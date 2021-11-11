package com.hanssecurity.uaa.repository;

import com.hanssecurity.uaa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author hans
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findOptionalByUsername(String username);

    Optional<User> findOptionalByEmail(String email);

    long countByUsername(String username);

    long countByEmail(String email);

    long countByMobile(String mobile);
}
