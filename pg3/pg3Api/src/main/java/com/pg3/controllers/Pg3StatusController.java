package com.pg3.controllers;

import com.pg3.security.JwtTokenUtil;
import com.pg3.services.DeliveryManService;
import com.pg3.services.DeliveryService;
import com.pg3.services.SiteMessageService;
import com.pg3.services.UserService;
import com.pg3.util.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
@CrossOrigin(origins = {"http://192.168.0.105:5500"})
public class Pg3StatusController {
    @Autowired
    private DeliveryService  deliveryService;

    @Autowired
    private UserService userService;

    @Autowired
    private SiteMessageService messageService;

    @Autowired
    private DeliveryManService  deliveryManService;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping("/delivery")
    public ResponseEntity getDeliveryStatus(){
        try {
            if (!getPermission()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(deliveryService.getStatus());
        }catch(Exception e) {
            return new ResponseEntity<>(new ResponseError("Internal server error" + e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user")
    public ResponseEntity getUserStatus(){
        try {
            if (!getPermission()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(userService.getStatus());
        }catch(Exception e) {
            return new ResponseEntity<>(new ResponseError("Internal server error" + e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deliveryMan")
    public ResponseEntity getDeliveryManStatus(){
        try {
            if (!getPermission()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(deliveryManService.getStatus());
        }catch(Exception e) {
            return new ResponseEntity<>(new ResponseError("Internal server error" + e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/message")
    public ResponseEntity getMessageStatus(){
        try {
            if (!getPermission()) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(messageService.getStatus());
        }catch(Exception e) {
            return new ResponseEntity<>(new ResponseError("Internal server error" + e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private boolean getPermission(){
        if(jwtUtil.getCurrentRequestUser().getAdmin() == 0){
            return false;
        }

        return true;
    }

}
