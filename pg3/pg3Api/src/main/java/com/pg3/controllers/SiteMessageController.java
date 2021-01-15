package com.pg3.controllers;

import com.pg3.models.SiteMessage;
import com.pg3.security.JwtTokenUtil;
import com.pg3.services.SiteMessageService;
import com.pg3.util.ClientException;
import com.pg3.util.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/message")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://192.168.0.105:5500"}, exposedHeaders = "Location")
public class SiteMessageController {

    @Autowired
    private SiteMessageService service;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping
    public ResponseEntity get(
            @PageableDefault(size = 5) Pageable p,
            @RequestParam(value = "read", required = false) Integer read,
            @RequestParam(value = "name", required = false) String name){

        try {
            if (!getPermission()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.get(p, read, name));
        } catch(Exception ex) {
            return new ResponseEntity(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) {
        try {
            if (!getPermission()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.getMessageById(id));
        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping()
    public ResponseEntity post(@RequestBody() SiteMessage msg){
        try{
            SiteMessage m = service.createMessage(msg);
            URI location = getUri(m.getId());
            return ResponseEntity.created(location).build();

        } catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@RequestBody() SiteMessage msg, @PathVariable("id") Long id){
        try {
            if(!getPermission()){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            service.updateMessage(id, msg);
            return ResponseEntity.ok().build();
        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        /* try {
            if(!getPermission()){
                return new ResponseEntity(new ResponseError("you does not have permission"), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok().build(service.deleteMessage(id));
        }catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        } */

        return new ResponseEntity(new ResponseError("not implemented", HttpStatus.NOT_IMPLEMENTED), HttpStatus.NOT_IMPLEMENTED);
    }

    private boolean getPermission(){
        if(jwtUtil.getCurrentRequestUser().getAdmin() == 0){
            return false;
        }

        return true;
    }

    private URI getUri(long id){
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
