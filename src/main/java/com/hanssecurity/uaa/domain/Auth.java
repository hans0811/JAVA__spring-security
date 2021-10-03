package com.hanssecurity.uaa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hans
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Auth implements Serializable {
    private String accessToken;
    private String refreshToken;
}
