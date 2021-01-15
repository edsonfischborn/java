package com.pg3.specification;

import com.pg3.models.Delivery;
import com.pg3.models.SiteMessage;
import org.springframework.data.jpa.domain.Specification;

public class DeliverySpecification {
    public static Specification<Delivery> findByDeliveryManId(Long id){
        if(id == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.equal(root.get("deliveryManId"), id);
        };
    }

    public static Specification<Delivery> findByUserId(Long id){
        if(id == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.equal(root.get("userId"), id);
        };
    }

    public static Specification<Delivery> findByDeliveryState(Long state){
        if(state == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.equal(root.get("deliveryState"), state);
        };
    }

    public static Specification<Delivery> findByReceiverName(String name){
        if(name == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.like(root.get("receiverName"), "%" + name + "%" );
        };
    }
}
