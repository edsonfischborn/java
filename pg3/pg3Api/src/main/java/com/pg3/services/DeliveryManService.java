package com.pg3.services;

import com.pg3.dto.DeliveryManStatusDto;
import com.pg3.models.DeliveryMan;
import com.pg3.repositories.DeliveryManRepository;
import com.pg3.repositories.DeliveryRepository;
import com.pg3.specification.DeliveryManSpecification;
import com.pg3.util.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryManService {

    @Autowired
    private DeliveryManRepository repo;

    @Autowired
    private DeliveryRepository deliveryRepo;

    public Page<DeliveryMan> get(Integer active, String name, String cpf, Pageable p){
        Specification<DeliveryMan> spec =
                Specification.where(DeliveryManSpecification.findByActive(active))
                    .and(DeliveryManSpecification.findByName(name))
                        .and(DeliveryManSpecification.findByCpf(cpf));
        return repo.findAll(spec, p);
    }

    public DeliveryMan getDeliveryManById(long id) throws ClientException {
        Optional<DeliveryMan> op = repo.findById(id);

        if(op.isPresent()) {
            return op.get();
        }

        throw new ClientException("The delivery man " + id + " does not exists", HttpStatus.NOT_FOUND);
    }

    public DeliveryMan createDeliveryMan(DeliveryMan d) throws ClientException {
        if(repo.findByEmail(d.getEmail()) != null){
            throw new ClientException("e-mail " + d.getEmail()  + " already in use", HttpStatus.BAD_REQUEST);
        }

        if(repo.findByCpf(d.getCpf()) != null) {
            throw new ClientException("CPF " + d.getCpf() + " already in use", HttpStatus.BAD_REQUEST);
        }

        return repo.save(d);
    }

    public void updateDeliveryMan(Long id, DeliveryMan del) throws ClientException {
        DeliveryMan d = getDeliveryManById(id);
        DeliveryMan validateEmailUser = repo.findByEmail(del.getEmail());
        DeliveryMan validateCpfUser = repo.findByCpf(del.getCpf());

        if(validateCpfUser != null && validateCpfUser.getId() != id){
            throw new ClientException("CPF " + del.getCpf() + " already in use", HttpStatus.BAD_REQUEST);
        }

        if(validateEmailUser != null && validateEmailUser.getId() != id){
            throw new ClientException("e-mail " + del.getEmail() + " already in use", HttpStatus.BAD_REQUEST);
        }

        d.setEmail(del.getEmail());
        d.setName(del.getName());
        d.setPhone(del.getPhone());
        d.setCpf(del.getCpf());

        if(del.getActive() == 0) {
            List pendingDeliveries = deliveryRepo.findAllByDeliveryStateAndDeliveryManId(1, id);

            if (pendingDeliveries.size() > 0) {
                throw new ClientException("the Delivery man has pending deliveries", HttpStatus.BAD_REQUEST);
            }
        }

        d.setActive(del.getActive());

        repo.save(d);
    }

    public void deleteDeliveryMan(Long id) throws ClientException {
        DeliveryMan user = getDeliveryManById(id);

        List pendingDeliveries = deliveryRepo.findAllByDeliveryStateAndDeliveryManId(1, id);

        if( pendingDeliveries.size() == 0 && user.getActive() == 1){
            user.setActive(0);
            repo.save(user);
        } else {
            if(pendingDeliveries.size() > 0){
                throw new ClientException("the Delivery man has pending deliveries", HttpStatus.BAD_REQUEST);
            } else {
                throw new ClientException("the Delivery man already inactive", HttpStatus.BAD_REQUEST);
            }
        }
    }

    public DeliveryManStatusDto getStatus(){
        DeliveryManStatusDto status = new DeliveryManStatusDto();

        status.setAllDeliveryMan(repo.count());
        status.setActiveDeliveryMan(repo.countByActive(1));
        status.setInactiveDeliveryMan(repo.countByActive(0));

        return status;
    }
}
