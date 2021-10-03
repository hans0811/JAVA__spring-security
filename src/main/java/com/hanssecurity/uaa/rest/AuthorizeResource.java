package com.hanssecurity.uaa.rest;

import com.hanssecurity.uaa.domain.Auth;
import com.hanssecurity.uaa.domain.User;
import com.hanssecurity.uaa.domain.dto.LoginDto;
import com.hanssecurity.uaa.domain.dto.UserDto;
import com.hanssecurity.uaa.exception.DuplicateProblem;
import com.hanssecurity.uaa.service.UserService;
import com.hanssecurity.uaa.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

/**
 * @author hans
 */
@RequiredArgsConstructor
@RequestMapping("/authorize")
@RestController
public class AuthorizeResource {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public void register(@Valid @RequestBody UserDto userDto){
        // 1. check username, email, mobile is unique
        if(userService.isUsernameExisted(userDto.getUsername())){
            throw new DuplicateProblem("username has been used");
        }

        if(userService.isUsernameExisted(userDto.getEmail())){
            throw new DuplicateProblem("Email has been used");
        }

        if(userService.isUsernameExisted(userDto.getMobile())){
            throw new DuplicateProblem("Mobile has been used");
        }

        val user = User.builder()
                .username(userDto.getUsername())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .mobile(userDto.getMobile())
                .password(userDto.getPassword())
                .build();

        // 2. userDto convert to User, then save and give default Role(ROLE_USER)
        userService.register(user);
    }

    @PostMapping("/token")
    public Auth login(@Valid @RequestBody LoginDto loginDto) throws Exception{
        return userService.login(loginDto.getUsername(), loginDto.getPassword());
    }

    @PostMapping("/token/refresh")
    public Auth refreshToken(@RequestHeader(name = "Authorization") String authorization,
                             @RequestParam String refreshToken) throws AccessDeniedException{
        val PREFIX = "Bearer";
        val accessToken = authorization.replace(PREFIX, "");
        if(jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessTokenWithoutExpiration(accessToken)){
            return new Auth(jwtUtil.createAccessTokenWithRefreshToken(refreshToken), refreshToken);
        }

        throw new AccessDeniedException("Viist Deny");

    }



}
