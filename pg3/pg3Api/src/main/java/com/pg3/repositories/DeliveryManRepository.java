package com.pg3.repositories;

import com.pg3.models.DeliveryMan;
import com.pg3.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DeliveryManRepository extends PagingAndSortingRepository<DeliveryMan, Long>, JpaSpecificationExecutor<DeliveryMan> {
    List<DeliveryMan> findByActive(Integer active);

    List<DeliveryMan> findByActive(Integer active, Pageable p);

    DeliveryMan findByEmail(String email);

    DeliveryMan findByCpf(String cpf);

    Long countByActive(int active);
}
