package com.hanssecurity.uaa.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hans
 */
@Data
public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String name;
}
