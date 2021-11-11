package com.hanssecurity.uaa.service.admin;

import com.hanssecurity.uaa.annotaion.RoleAdminOrRead;
import com.hanssecurity.uaa.domain.Permission;
import com.hanssecurity.uaa.repository.PermissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hans
 */

@RequiredArgsConstructor
@Service
public class PermissionAdminService {

    private final PermissionRepo permissionRepo;

    /**
     * 取得全部用户列表
     *
     * @return 全部用户列表
     */
    @RoleAdminOrRead
    public List<Permission> findAll() {
        return permissionRepo.findAll();
    }


}
