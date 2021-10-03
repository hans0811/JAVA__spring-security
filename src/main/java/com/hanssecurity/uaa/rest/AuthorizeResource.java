package com.hanssecurity.uaa.rest;

import com.hanssecurity.uaa.domain.Auth;
import com.hanssecurity.uaa.domain.dto.LoginDto;
import com.hanssecurity.uaa.domain.dto.UserDto;
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
    public UserDto register(@Valid @RequestBody UserDto userDto){
        return userDto;
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
