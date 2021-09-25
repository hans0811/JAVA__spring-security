package com.hanssecurity.uaa.domain.dto;

import com.hanssecurity.uaa.annotaion.PasswordMatch;
import com.hanssecurity.uaa.annotaion.ValidEmail;
import com.hanssecurity.uaa.annotaion.ValidPassword;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author hans
 */
@PasswordMatch
@Data
public class UserDto implements Serializable {

    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "Username between 4 and 50")
    private String username;

    @NotNull
//    @NotBlank
//    @Size(min = 4, max = 20, message = "Password between 4 and 20")
    @ValidPassword
    private String password;

    @NotNull
//    @NotBlank
//    @Size(min = 4, max = 20, message = "Password between 4 and 20")
//    @ValidPassword
    private String matchingPassword;
    @NotNull
    @ValidEmail
    //@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 20, message = "name between 4 and 20")
    private String name;
}
