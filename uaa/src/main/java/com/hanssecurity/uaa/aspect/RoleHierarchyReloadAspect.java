package com.hanssecurity.uaa.aspect;

import com.hanssecurity.uaa.security.RoleHierarchy.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

/**
 * @author hans
 */

@Slf4j
@RequiredArgsConstructor
@Aspect
public class RoleHierarchyReloadAspect {

    private final RoleHierarchyImpl roleHierarchy;
    private final RoleHierarchyService roleHierarchyService;

    @Pointcut("execution(* com.hanssecurity.uaa.service.admin.*.*(..))")
    public void applicationPackagePointCut(){}

    @AfterReturning("applicationPackagePointCut() && @annotation(com.hanssecurity.uaa.annotaion.ReloadRoleHierarchy)")
    public void reloadRoleHierarchy() {
        val roleMap = roleHierarchyService.getRoleHierarchyExpr();
        roleHierarchy.setHierarchy(roleMap);
        log.debug("RoleHierarchy Reloaded");
    }
}
