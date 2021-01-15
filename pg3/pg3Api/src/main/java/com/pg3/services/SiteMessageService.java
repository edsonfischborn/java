package com.pg3.services;

import com.pg3.dto.SiteMessageStatusDto;
import com.pg3.models.SiteMessage;
import com.pg3.repositories.SiteMessageRepository;
import com.pg3.specification.SiteMessageSpecification;
import com.pg3.util.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SiteMessageService {

    @Autowired
    private SiteMessageRepository repo;

    public Page<SiteMessage> get(Pageable p, Integer read, String name){
        Specification<SiteMessage> spec =
                Specification.where(SiteMessageSpecification.findByRead(read))
                        .and(SiteMessageSpecification.findByName(name));
       return repo.findAll(spec, p);
    }

    public SiteMessage getMessageById(Long id) throws ClientException{
        Optional<SiteMessage> msg = repo.findById(id);

        if(msg.isPresent()){
            return msg.get();
        }

        throw new ClientException("Message " + id + " does not exists", HttpStatus.NOT_FOUND);

    }

    public SiteMessage createMessage(SiteMessage msg){
        return repo.save(msg);
    }

    public void updateMessage(Long id, SiteMessage msg) throws ClientException {
        SiteMessage message = getMessageById(id);
        
        message.setName(msg.getName());
        message.setEmail(msg.getEmail());
        message.setMessage(msg.getMessage());
        message.setRead(msg.getRead());

        repo.save(message);
    }

    public void deleteMessage(Long id){
        repo.deleteById(id);
    }

    public SiteMessageStatusDto getStatus() {
        SiteMessageStatusDto status = new SiteMessageStatusDto();

        status.setAllMessages(repo.count());
        status.setReadMessages(repo.countByRead(1));
        status.setUnreadMessages(repo.countByRead(0));

        return status;
    }

}
