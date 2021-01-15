package com.pg3.controllers;

import com.pg3.models.Delivery;
import com.pg3.models.User;
import com.pg3.security.JwtTokenUtil;
import com.pg3.services.DeliveryService;
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
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://192.168.0.105:5500"}, exposedHeaders = "Location")
@RequestMapping("/delivery")
public class DeliveryController {
    @Autowired
    private DeliveryService service;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping
    public ResponseEntity get(
            @RequestParam(value = "deliveryManId", required = false) Long deliveryManId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "deliveryState", required = false) Long deliveryState,
            @RequestParam(value = "receiverName", required = false) String receiverName,
            @PageableDefault(size = 5) Pageable p){

        try {
            User loggedUser = jwtUtil.getCurrentRequestUser();

            if(loggedUser.getAdmin() == 0 && userId == null || loggedUser.getAdmin() == 0 && loggedUser.getId() != userId){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok((service.get(deliveryManId, userId, deliveryState, receiverName ,p)));
        }catch(Exception ex) {
            return new ResponseEntity<>(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id){
        try {
            if(!validateDeliveryAccess(id)){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.getDeliveryDetailDtoById(id));

        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());

        } catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/file/receiverdocument")
    public ResponseEntity get(@PathVariable(name = "id") Long id){
        try {
            if (!validateDeliveryAccess(id)) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.getBoxPdf(id));
        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());

        } catch(Exception ex) {
            return new ResponseEntity(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody() Delivery  d) {
        try {
            User loggedUser = jwtUtil.getCurrentRequestUser();

            if (loggedUser.getAdmin() == 0 && loggedUser.getId() != d.getUserId()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            Delivery delivery = service.createDelivery(d);
            URI location = getUri(delivery.getId());
            return ResponseEntity.created(location).build();

        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails" + ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@RequestBody() Delivery  delivery, @PathVariable("id") Long id){
        try {
            if(!validateDeliveryAccess(id)){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            service.updateDelivery(id, delivery);
            return ResponseEntity.ok().build();

        }catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        } catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        return new ResponseEntity(new ResponseError("not implemented", HttpStatus.NOT_IMPLEMENTED), HttpStatus.NOT_IMPLEMENTED);
    }

    private boolean validateDeliveryAccess(Long deliveryId) throws Exception{
        User loggedUser = jwtUtil.getCurrentRequestUser();
        Delivery d = service.getDeliveryById(deliveryId);

        if(d == null){
            return false;
        }

        if(loggedUser.getAdmin() == 0 && d.getUserId() != loggedUser.getId()){
            return false;
        }

        return true;
    }

    private URI getUri(long id){
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
