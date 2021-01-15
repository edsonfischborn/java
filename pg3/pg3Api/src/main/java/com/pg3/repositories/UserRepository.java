package com.pg3.repositories;

import com.pg3.models.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    User findByCpf(String cpf);

    Long countByActive(int active);
}
