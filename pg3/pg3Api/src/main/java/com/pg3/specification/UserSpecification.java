package com.pg3.specification;

import com.pg3.models.Delivery;
import com.pg3.models.DeliveryMan;
import com.pg3.models.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> findByActive(Integer active){
        if(active == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<User> findByName(String name){
        if(name == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.like(root.get("name"), "%" + name + "%" );
        };
    }

    public static Specification<User> findByCpf(String cpf){
        if(cpf == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.equal(root.get("cpf"), cpf );
        };
    }
}
