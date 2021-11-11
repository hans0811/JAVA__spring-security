package com.hanssecurity.uaa.security.RoleHierarchy;

import com.hanssecurity.uaa.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.hanssecurity.uaa.config.Constants.*;

/**
 * @author hans
 */

@RequiredArgsConstructor
@Service
public class RoleHierarchyService {

    private final RoleRepo roleRepo;

    public String getRoleHierarchyExpr() {
        val roles = roleRepo.findAll();
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream()
                        .map(permission -> role.getRoleName() + " > " + permission.getAuthority() + "\n"))
                .collect(Collectors.joining("", ROLE_ADMIN + " > " + ROLE_STAFF, "\n"));
    }
}