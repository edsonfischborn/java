package com.pg3.specification;

import com.pg3.models.DeliveryMan;
import org.springframework.data.jpa.domain.Specification;

public class DeliveryManSpecification {
    public static Specification<DeliveryMan> findByActive(Integer active){
        if(active == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<DeliveryMan> findByName(String name){
        if(name == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.like(root.get("name"), "%" + name + "%" );
        };
    }

    public static Specification<DeliveryMan> findByCpf(String cpf){
        if(cpf == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.equal(root.get("cpf"), cpf );
        };
    }
}
