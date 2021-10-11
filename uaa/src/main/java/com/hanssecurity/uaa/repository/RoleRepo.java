package com.hanssecurity.uaa.repository;

import com.hanssecurity.uaa.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @author hans
 */
@Repository
public interface  RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findOptionalByAuthority(String authority);

}
