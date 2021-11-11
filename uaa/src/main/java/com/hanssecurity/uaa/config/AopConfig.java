package com.hanssecurity.uaa.config;

import com.hanssecurity.uaa.aspect.RoleHierarchyReloadAspect;
import com.hanssecurity.uaa.security.RoleHierarchy.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

/**
 * @author hans
 */

@RequiredArgsConstructor
@EnableAspectJAutoProxy
@Configuration
public class AopConfig {

    private final RoleHierarchyImpl roleHierarchy;
    private final RoleHierarchyService roleHierarchyService;

    @Bean
    public RoleHierarchyReloadAspect roleHierarchyReloadAspect() {
        return new RoleHierarchyReloadAspect(roleHierarchy, roleHierarchyService);
    }
}
