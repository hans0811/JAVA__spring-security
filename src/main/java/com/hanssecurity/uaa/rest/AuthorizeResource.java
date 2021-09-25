package com.hanssecurity.uaa.rest;

import com.hanssecurity.uaa.domain.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author hans
 */

@RequestMapping("/authorize")
@RestController
public class AuthorizeResource {

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto userDto){
        return userDto;
    }

}
