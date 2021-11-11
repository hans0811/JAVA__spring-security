package com.hanssecurity.uaa.domain.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hanssecurity.uaa.annotaion.ValidEmail;
import com.hanssecurity.uaa.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author hans
 */


@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateUserDto implements Serializable {


    private static final long serialVersionUID = 1L;

    @NotNull
    @Pattern(regexp = Constants.PATTERN_USERNAME)
    private String username;

    @ValidEmail
    @NotNull
    @Size(min = 1)
    private String email;

    @Pattern(regexp = Constants.PATTERN_MOBILE)
    @NotNull
    private String mobile;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @Builder.Default
    @NotNull
    private boolean usingMfa = true;
}
