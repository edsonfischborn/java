package com.pg3.services;

import com.pg3.models.DeliveryState;
import com.pg3.repositories.DeliveryStateRepository;
import com.pg3.util.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryStateService {

    @Autowired
    private DeliveryStateRepository repo;

    public Page<DeliveryState> get(Pageable p){
       return repo.findAll(p);
    }

    public DeliveryState getDeliveryStateById(Long id) throws ClientException{
        Optional<DeliveryState> op = repo.findById(id);

        if(op.isPresent()){
            return op.get();
        }

        throw new ClientException("Delivery state " + id + " does not exists", HttpStatus.NOT_FOUND);
    }
}
