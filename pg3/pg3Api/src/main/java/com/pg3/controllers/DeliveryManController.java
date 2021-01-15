package com.pg3.controllers;

import com.pg3.models.DeliveryMan;
import com.pg3.security.JwtTokenUtil;
import com.pg3.services.DeliveryManService;
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
@RequestMapping("/deliveryman")
@CrossOrigin(origins = {"http://192.168.0.105:5500"}, exposedHeaders = "Location")
public class DeliveryManController {
    @Autowired
    private DeliveryManService service;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping
    public ResponseEntity get(
            @RequestParam(value = "active", required = false) Integer active,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "name", required = false) String name,
            @PageableDefault(size = 5) Pageable p){

        try {
            if (!getPermission()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.get(active, name, cpf, p));
        }catch(Exception e) {
            return new ResponseEntity<>(new ResponseError("Internal server error" , HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id){
        try {
            if(!getPermission()){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.getDeliveryManById(id));

        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody() DeliveryMan  user){
        try {
            if(!getPermission()){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            DeliveryMan d = service.createDeliveryMan(user);
            URI location = getUri(d.getId());
            return ResponseEntity.created(location).build();
        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        } catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@RequestBody() DeliveryMan  user, @PathVariable("id") Long id){
        try {
            if(!getPermission()){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            service.updateDeliveryMan(id, user);
            return ResponseEntity.ok().build();

        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        if(!getPermission()){
            return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
        }

        try {
            service.deleteDeliveryMan(id);
            return ResponseEntity.ok().build();

        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

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
