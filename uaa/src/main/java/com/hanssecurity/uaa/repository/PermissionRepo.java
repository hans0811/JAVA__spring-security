package com.hanssecurity.uaa.repository;

import com.hanssecurity.uaa.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * @author hans
 */

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long>, QuerydslPredicateExecutor<Permission> {

    @Override
    Optional<Permission> findById(Long aLong);

    Set<Permission> findByIdIn(Set<Long> ids);

    @Query("select p from Permission p left join p.roles where p.id <> ?1")
    Set<Permission> findAvailablePermission(Long roleId);

    long countByAuthorityAndIdNot(String authority, Long Id);
}
