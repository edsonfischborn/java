package com.pg3.services;

import com.pg3.dto.UserDto;
import com.pg3.dto.UserStatusDto;
import com.pg3.models.User;
import com.pg3.repositories.DeliveryRepository;
import com.pg3.repositories.UserRepository;
import com.pg3.specification.UserSpecification;
import com.pg3.util.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private DeliveryRepository deliveryRepo;

    public Page<UserDto> get(Integer active, String name, String cpf, Pageable p){
        Specification<User> spec =
                Specification.where(UserSpecification.findByActive(active))
                .and(UserSpecification.findByName(name))
                .and(UserSpecification.findByCpf(cpf));

        return repo.findAll(spec, p).map(user -> new UserDto(user));
    }

    public User getUserById(Long id) throws ClientException {
        Optional<User> usr = repo.findById(id);

        if(usr.isPresent()) {
            return usr.get();
        }

        throw new ClientException("User " + id + " does not exists", HttpStatus.NOT_FOUND);
    }

    public UserDto getUserDtoById(long id) throws ClientException {
        return new UserDto(getUserById(id));
    }

    public User createUser(User user) throws ClientException{
        if(repo.findByEmail(user.getEmail()) != null){
            throw new ClientException("e-mail " + user.getEmail()  + " already in use", HttpStatus.BAD_REQUEST);
        }

        if(repo.findByCpf(user.getCpf()) != null){
            throw new ClientException("CPF " + user.getCpf() + " already in use", HttpStatus.BAD_REQUEST);
        }

        if(user.getPassword().length() < 6){
            throw new ClientException("the password do not less than 6 characters", HttpStatus.BAD_REQUEST);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        user.setPassword(encoder.encode((user.getPassword())));
        user.setAdmin(0);

        return repo.save(user);
    }

    public void updateUser(Long id, User user) throws ClientException{
        User usr = getUserById(id);

        if(user.getAdmin() == 1){
            throw new ClientException("Invalid user", HttpStatus.BAD_REQUEST);
        }

        User validateEmailUser = repo.findByEmail(user.getEmail());
        User validateCpfUser = repo.findByCpf(user.getCpf());

        if(validateCpfUser != null && validateCpfUser.getId() != id){
            throw new ClientException("CPF " + user.getCpf() + " already in use", HttpStatus.BAD_REQUEST);
        }

        if(validateEmailUser != null && validateEmailUser.getId() != id){
            throw new ClientException("e-mail " + user.getEmail() + " already in use", HttpStatus.BAD_REQUEST);
        }

        usr.setName(user.getName());
        usr.setEmail(user.getEmail());
        usr.setPhone(user.getPhone());
        usr.setCpf(user.getCpf());

        if(user.getActive() == 0) {
            List pendingDeliveries = deliveryRepo.findAllByDeliveryStateAndUserId(1, id);

            if (pendingDeliveries.size() > 0) {
                throw new ClientException("the user has pending deliveries", HttpStatus.BAD_REQUEST);
            }
        }

        usr.setActive(user.getActive());

        if(user.getPassword().length() > 5){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
            usr.setPassword(encoder.encode(user.getPassword()));
        }

        repo.save(usr);
    }

    public void deleteUser(Long id) throws Exception{
        User user = getUserById(id);

        if(user.getAdmin() == 1){
            throw new ClientException("Invalid user", HttpStatus.BAD_REQUEST);
        }

        List pendingDeliveries = deliveryRepo.findAllByDeliveryStateAndUserId(1, id);

        if( pendingDeliveries.size() == 0 && user.getActive() == 1){
            user.setActive(0);
            repo.save(user);
        } else {
            if(pendingDeliveries.size() > 0){
                throw new ClientException("the user has pending deliveries", HttpStatus.BAD_REQUEST);
            } else {
                throw new ClientException("the user already inactive", HttpStatus.BAD_REQUEST);
            }
        }

    }

    public UserStatusDto getStatus() {
        UserStatusDto status = new UserStatusDto();

        status.setActiveUsers(repo.countByActive(1));
        status.setInactiveUsers(repo.countByActive(0));
        status.setAllUsers(repo.count());

        return status;
    }
}
