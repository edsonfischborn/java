package com.pg3.repositories;

import com.pg3.models.Delivery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeliveryRepository extends PagingAndSortingRepository<Delivery, Long>, JpaSpecificationExecutor<Delivery> {
    List<Delivery> findByDeliveryManId(long deliveryManId, Pageable pageable);

    List<Delivery> findAllByUserId(long userId, Pageable pageable);

    List<Delivery> findAllByDeliveryManId(long userId);

    List<Delivery> findAllByDeliveryStateAndUserId(long deliveryState, long userId);

    List<Delivery> findAllByDeliveryStateAndDeliveryManId(long deliveryState, long deliveryManId);

    Long countAllByDeliveryState(long deliveryState);

    @Query(value = "SELECT COUNT(id) FROM Delivery d WHERE createdAt LIKE :date%", nativeQuery = true)
    Long countAllLikeDate(@Param("date") String date);

    @Query(value = "SELECT COUNT(id) FROM Delivery d WHERE d.createdAt LIKE :date% AND d.deliveryState = :deliveryState", nativeQuery = true)
    Long countAllLikeCreatedAtAndDeliveryState(@Param("date") String date, @Param("deliveryState") Long deliveryState);

    @Query(value = "SELECT COUNT(id) FROM Delivery d WHERE d.updatedAt LIKE :date% AND d.deliveryState = :deliveryState", nativeQuery = true)
    Long countAllLikeUpdatedAtAndDeliveryState(@Param("date") String date, @Param("deliveryState") Long deliveryState);
}
