package com.pg3.services;

import com.pg3.dto.*;
import com.pg3.models.Delivery;
import com.pg3.models.DeliveryMan;
import com.pg3.models.DeliveryState;
import com.pg3.models.User;
import com.pg3.repositories.DeliveryManRepository;
import com.pg3.repositories.DeliveryRepository;
import com.pg3.repositories.DeliveryStateRepository;
import com.pg3.repositories.UserRepository;
import com.pg3.specification.DeliverySpecification;
import com.pg3.util.ClientException;
import com.pg3.util.CreateDocument;
import com.pg3.util.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository repo;

    @Autowired
    private DeliveryManRepository deliveryManRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    DeliveryStateRepository deliveryStateRepo;

    public Page<DeliveryDto> get(Long deliveryManId, Long userId, Long deliveryState, String receiverName, Pageable p){
        Specification<Delivery> spec =
                Specification.where(DeliverySpecification.findByDeliveryManId(deliveryManId))
                .and(DeliverySpecification.findByUserId(userId))
                .and(DeliverySpecification.findByDeliveryState(deliveryState))
                .and(DeliverySpecification.findByReceiverName(receiverName));

        return repo.findAll(spec, p).map(delivery -> this.getDeliveryDto(delivery));
    }

    public Delivery getDeliveryById(long id) throws ClientException {
        Optional<Delivery> delivery = repo.findById(id);

        if(delivery.isPresent()){
            return delivery.get();
        }

        throw new ClientException("Delivery " + id + " does not exists", HttpStatus.NOT_FOUND);
    }

    public DeliveryDetailDto getDeliveryDetailDtoById(Long id) throws ClientException{
        return getDeliveryDetailDto(getDeliveryById(id));
    }

    public Delivery createDelivery(Delivery d) throws ClientException{
        User user = userRepository.findById(d.getUserId()).get();

        if(user == null) {
            throw new ClientException("User " + d.getUserId() + " not found", HttpStatus.BAD_REQUEST);
        }else if(user.getActive() == 0) {
            throw new ClientException("Invalid user, Inactive!", HttpStatus.BAD_REQUEST);
        }

        if(d.getDeliveryManId() == 0){
            List<DeliveryMan> deliveryManList = deliveryManRepository.findByActive(1);

            deliveryManList.sort((user1, user2) -> {
                List<Delivery>  user1Deliveries = repo.findAllByDeliveryManId(user1.getId());
                List<Delivery>  user2Deliveries = repo.findAllByDeliveryManId(user2.getId());

                if ( user1Deliveries.size() < user2Deliveries.size() ) {
                    return -1;
                }
                if ( user1Deliveries.size()  > user2Deliveries.size() ) {
                    return 1;
                }
                return 0;
            });

            DeliveryMan deliveryMan = deliveryManList.get(0);

            if(deliveryMan == null) {
                throw new ClientException("Delivery man " + d.getDeliveryManId() + " not found", HttpStatus.BAD_REQUEST);
            }

            d.setDeliveryManId(deliveryMan.getId());
        } else {
            DeliveryMan deliveryMan = deliveryManRepository.findById(d.getDeliveryManId()).get();

            if(deliveryMan == null) {
                throw new ClientException("Delivery man " + d.getDeliveryManId() + " not found", HttpStatus.BAD_REQUEST);
            }else if(deliveryMan.getActive() == 0) {
                throw new ClientException("Invalid delivery man, Inactive!", HttpStatus.BAD_REQUEST);
            }
        }

        // Set default state to new delivery
        d.setDeliveryState(1);

        return repo.save(d);
    }

    public void updateDelivery(Long id, Delivery delivery) throws ClientException{
        User user = userRepository.findById(delivery.getUserId()).get();
        DeliveryMan deliveryMan = deliveryManRepository.findById(delivery.getDeliveryManId()).get();
        Delivery d = getDeliveryById(id);

        if(user == null) {
            throw new ClientException("User " + d.getUserId() + " not found", HttpStatus.BAD_REQUEST);
        }else if(user.getActive() == 0) {
            throw new ClientException("Invalid user, Inactive!", HttpStatus.BAD_REQUEST);
        }

        if(deliveryMan == null) {
            throw new ClientException("Delivery man " + d.getDeliveryManId() + " not found", HttpStatus.BAD_REQUEST);
        }else if(deliveryMan.getActive() == 0) {
            throw new ClientException("Invalid delivery man, Inactive!", HttpStatus.BAD_REQUEST);
        }

        d.setUserId(delivery.getUserId());
        d.setDeliveryManId(delivery.getDeliveryManId());
        d.setDeliveryState(delivery.getDeliveryState());
        d.setDescription(delivery.getDescription());
        d.setStreet(delivery.getStreet());
        d.setNumber(delivery.getNumber());
        d.setComplement(delivery.getComplement());
        d.setZipCode(delivery.getZipCode());
        d.setCity(delivery.getCity());
        d.setDistrict(delivery.getDistrict());
        d.setState(delivery.getState());
        d.setReceiverName(delivery.getReceiverName());
        d.setReceiverCpf(delivery.getReceiverCpf());
        d.setReceiverContact(delivery.getReceiverContact());
        d.setReceiverSignature(delivery.getReceiverSignature());

        repo.save(d);
    }

    public void deleteDelivery(Long id){
        /*repo.deleteById(id);*/
    }

    // File
    public FileResponse getBoxPdf(Long deliveryId) throws Exception{
        CreateDocument createDocument = new CreateDocument();
        DeliveryDetailDto d = getDeliveryDetailDtoById(deliveryId);

        String base64 = createDocument.createReceiverDocument(
                "DECLARAÇÃO DE RECEBIMENTO DE ENCOMENDA",
                    "Eu, " + d.getReceiverName() +
                            ", CPF " + d.getReceiverCpf() +
                            ", declaro ter recebido a encomenda de número " + d.getId() +
                            ", enviada pelo usuário " + d.getUser().getName() +
                            ", CPF " + d.getUser().getCpf() + ". E estou ciente que a FAST PGIII, não é responsável pelo item enviado e sim por manter a integridade do mesmo do início ao fim da entrega.",
                        d.getReceiverName(),
                "Entregador - PGIII, " + d.getDeliveryMan().getName()
        );

        return new FileResponse(base64);
    }

    // Dto mount
    public DeliveryStatusDto getStatus(){
        DeliveryStatusDto status = new DeliveryStatusDto();

        status.setAllDeliveries(repo.count());
        status.setDeliveredDeliveries(repo.countAllByDeliveryState(2));
        status.setPendingDeliveries(repo.countAllByDeliveryState(1));
        status.setProblemsDeliveries(repo.countAllByDeliveryState(3));

        ArrayList<String> dates = lastDaysDates(5);

        for(int i = dates.size() - 1; i >= 0; i-- ){
            String[] delivered = {dates.get(i), repo.countAllLikeDate(dates.get(i)).toString()};
            String[] problems = {dates.get(i), repo.countAllLikeUpdatedAtAndDeliveryState(dates.get(i), Long.parseLong("3")).toString()};
            status.getLastDaysDeliveredDeliveries().add(delivered);
            status.getLastDaysProblemsDeliveries().add(problems);
        }


        return status;
    }

    private ArrayList<String> lastDaysDates(int daysCount){
        ArrayList<String> lastDays = new ArrayList<>();

        GregorianCalendar cal = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        int day = cal.get(GregorianCalendar.DAY_OF_MONTH);
        int month = cal.get(GregorianCalendar.MONTH);
        int year = cal.get(GregorianCalendar.YEAR);

        for(int i=day; i > (day-daysCount); i--){
            cal.set(year, month, i);
            Date date = cal.getTime();

            lastDays.add(formatter.format(date));
        }

        return lastDays;
    }

    private DeliveryDto getDeliveryDto(Delivery delivery){
        return new DeliveryDto(
                delivery,
                getDeliveryState(delivery.getDeliveryState())
        );
    }

   private DeliveryDetailDto getDeliveryDetailDto(Delivery delivery){
        return new DeliveryDetailDto(
                delivery,
                getDeliveryState(delivery.getDeliveryState()),
                getUserDto(delivery.getUserId()),
                getDeliveryMan(delivery.getDeliveryManId())
        );
    }

    private UserDto getUserDto(long id){
        User user = userRepository.findById(id).get();
        return new UserDto(user);
    }

    private DeliveryMan getDeliveryMan(long id){
        return deliveryManRepository.findById(id).get();
    }

    private DeliveryState getDeliveryState(long id){
        return deliveryStateRepo.findById(id).get();
    }
}
