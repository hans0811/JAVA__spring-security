package com.hanssecurity.uaa.repository;

import com.hanssecurity.uaa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author hans
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findOptionalByUsername(String username);
    long countByUsername(String username);
    long countByEmail(String email);
    long countByMobile(String mobile);
}
