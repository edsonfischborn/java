package com.pg3.specification;

import com.pg3.models.SiteMessage;
import org.springframework.data.jpa.domain.Specification;

public class SiteMessageSpecification {
    public static Specification<SiteMessage> findByRead(Integer read){
        if(read == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.equal(root.get("read"), read);
        };
    }

    public static Specification<SiteMessage> findByName(String name){
        if(name == null) {
            return null;
        }

        return (root, query, cb) -> {
            return cb.like(root.get("name"), "%" + name + "%" );
        };
    }
}
