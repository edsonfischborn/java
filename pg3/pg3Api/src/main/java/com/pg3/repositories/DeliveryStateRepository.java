package com.pg3.repositories;

import com.pg3.models.DeliveryState;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryStateRepository extends PagingAndSortingRepository<DeliveryState, Long> {
}
