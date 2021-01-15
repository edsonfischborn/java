package com.pg3.controllers;

import com.pg3.models.User;
import com.pg3.security.JwtTokenUtil;
import com.pg3.services.UserService;
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
@RequestMapping("/user")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://192.168.0.105:5500"}, exposedHeaders = "Location")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping
    public ResponseEntity get(
            @PageableDefault(size = 5) Pageable p,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "active", required = false) Integer active){

        try {
            if (jwtUtil.getCurrentRequestUser().getAdmin() == 0) {
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.get(active, name, cpf, p));
        } catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id){
        try {
            if(!getUserPermission(id)){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(service.getUserDtoById(id));

        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        }catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody() User user){
        try {
            User s = service.createUser(user);
            URI location = getUri(s.getId());
            return ResponseEntity.created(location).build();

        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        } catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@RequestBody() User user, @PathVariable("id") Long id){
        try {
            if(!getUserPermission(id)){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            service.updateUser(id, user);
            return ResponseEntity.ok().build();
        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        } catch(Exception ex){
            return new ResponseEntity<>(new ResponseError("validation fails", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        try{
            if(!getUserPermission(id)){
                return new ResponseEntity(new ResponseError("you does not have permission", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
            }

            service.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch(ClientException ex){
            return new ResponseEntity<>(new ResponseError(ex.getMessage(), ex.getStatus()), ex.getStatus());
        } catch(Exception ex){
            return new ResponseEntity(new ResponseError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private boolean getUserPermission(Long id){
        User loggedUser = jwtUtil.getCurrentRequestUser();

        if(loggedUser.getAdmin() == 0 && loggedUser.getId() != id){
            return  false;
        }

        return true;
    }

    private URI getUri(long id){
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

}
