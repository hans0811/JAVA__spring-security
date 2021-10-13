package com.hanssecurity.uaa.rest;

import com.hanssecurity.uaa.domain.Auth;
import com.hanssecurity.uaa.domain.MfaType;
import com.hanssecurity.uaa.domain.User;
import com.hanssecurity.uaa.domain.dto.LoginDto;
import com.hanssecurity.uaa.domain.dto.SendTotpDto;
import com.hanssecurity.uaa.domain.dto.UserDto;
import com.hanssecurity.uaa.domain.dto.VerifyTotpDto;
import com.hanssecurity.uaa.exception.*;
import com.hanssecurity.uaa.service.EmailService;
import com.hanssecurity.uaa.service.SmsService;
import com.hanssecurity.uaa.service.UserCacheService;
import com.hanssecurity.uaa.service.UserService;
import com.hanssecurity.uaa.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

/**
 * @author hans
 */
@RequiredArgsConstructor
@RequestMapping("/authorize")
@RestController
@CrossOrigin(value = "http://localhost:4001")
public class AuthorizeResource {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserCacheService userCacheService;
    private final SmsService smsService;
    private final EmailService emailService;

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) throws Exception{
        return userService.findOptionalByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword())
                    .map(user -> {
                        // 1. upgrade password
                        //userService.updatePassword(user, loginDto.getPassword());
                        // 2. validate enable
                        if(!user.isEnabled()){
                            throw new UserNotEnabledProblem();
                        }
                        if(!user.isAccountNonExpired()){
                            throw new UserAccountExpiredProblem();
                        }
                        if(!user.isAccountNonLocked()){
                            throw new UserAccountLockedProblem();
                        }
                          // 3. check MFA, if false, it return token
                        if(!user.isUsingMfa()){
                            return ResponseEntity.ok().body(userService.login(loginDto.getUsername(), loginDto.getPassword()));
                        }
                        // 4. use MFA
                        val mfaId = userCacheService.cacheUser(user);
                        // 5. "X-Authenticate" : "mfa", "realm=" + mfaId
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .header("X-Authenticate", "mfa", "realm=" + mfaId)
                                .build();


                    })
                    .orElseThrow(() -> new BadCredentialsException("Error : username or password error!!!"));
        //return userService.login(loginDto.getUsername(), loginDto.getPassword());
    }

    // Optional<Optional<String>> -> use flatMap to String
    @PutMapping("/totp")
    public void sendTotp(@Valid @RequestBody SendTotpDto sendTotpDTO) {
        userCacheService.retrieveUser(sendTotpDTO.getMfaId())
                .flatMap(user -> userService.createTotp(user.getMfaKey()).map(totp -> Pair.of(user, totp)))
                .ifPresentOrElse(pair -> {
                    if(sendTotpDTO.getMfaType() == MfaType.SMS) {
                        smsService.send(pair.getFirst().getEmail(), pair.getSecond());
                    }else{
                        emailService.send(pair.getFirst().getEmail(), pair.getSecond());
                    }

                }, () -> {
                    throw new InvalidTotpProblem();
                });

    }

    @PostMapping("/totp")
    public Auth verifyTotp(@Valid @RequestBody VerifyTotpDto verifyTotpDto){

        return userCacheService.verifyTotp(verifyTotpDto.getMfaId(), verifyTotpDto.getCode())
                .map(user -> userService.login(user.getUsername(), user.getPassword()))
                .orElseThrow(() -> new InvalidTotpProblem());
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
