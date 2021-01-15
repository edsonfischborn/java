package com.pg3.repositories;

import com.pg3.models.SiteMessage;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SiteMessageRepository extends PagingAndSortingRepository<SiteMessage, Long>, JpaSpecificationExecutor<SiteMessage> {
    Long countByRead(int id);
}
