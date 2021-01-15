package com.pg3.controllers;

import com.pg3.security.JwtTokenUtil;
import com.pg3.services.DeliveryStateService;
import com.pg3.util.ClientException;
import com.pg3.util.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery/deliveryState")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://192.168.0.105:5500"}, exposedHeaders = "Location")
public class DeliveryStateController {

    @Autowired
    private DeliveryStateService service;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping
    public ResponseEntity get(@PageableDefault(size = 5) Pageable p){
        try {
            if (!getPermission()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.get(p));
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

            return ResponseEntity.ok(service.getDeliveryStateById(id));
        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private boolean getPermission(){
        if(jwtUtil.getCurrentRequestUser().getAdmin() == 0){
            return false;
        }

        return true;
    }

}
