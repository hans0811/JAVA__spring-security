package com.hanssecurity.uaa.annotaion;

import com.hanssecurity.uaa.config.Constants;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hans
 */

@Retention(RetentionPolicy.SOURCE)
@PreAuthorize("authentication.name == #user.username or " +
        "hasAnyAuthority('" + Constants.ROLE_ADMIN + "', '" + Constants.AUTHORITY_USER_UPDATE + "')")
public @interface RoleAdminOrSelfWithUserParam {
}
