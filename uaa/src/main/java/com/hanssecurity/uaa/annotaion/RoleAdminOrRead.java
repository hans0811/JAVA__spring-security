package com.hanssecurity.uaa.annotaion;

import com.hanssecurity.uaa.config.Constants;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hans
 */

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('" +
        Constants.ROLE_ADMIN +
        "', '" +
        Constants.AUTHORITY_USER_READ +
        "')")
public @interface RoleAdminOrRead {
}
