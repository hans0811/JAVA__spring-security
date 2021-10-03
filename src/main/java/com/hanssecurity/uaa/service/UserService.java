package com.hanssecurity.uaa.service;

import com.hanssecurity.uaa.config.Constants;
import com.hanssecurity.uaa.domain.Auth;
import com.hanssecurity.uaa.domain.User;
import com.hanssecurity.uaa.repository.RoleRepo;
import com.hanssecurity.uaa.repository.UserRepo;
import com.hanssecurity.uaa.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;


/**
 * @author hans
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User register(User user) {
        return roleRepo.findOptionalByAuthority(Constants.ROLE_USER)
                .map(role -> {
                    val userToSave = user
                            .withAuthorities(Set.of(role))
                            .withPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepo.save(userToSave);
                })
                .orElseThrow();
    }

    public Auth login(String username, String password) throws AuthenticationException {
        return userRepo.findOptionalByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> new Auth(
                        jwtUtil.createAccessToken(user),
                        jwtUtil.createRefreshToken(user)
                ))
                .orElseThrow(()-> new BadCredentialsException("username or password not exist"));
    }

    /**
     * check username is exist
     * @param username
     * @return
     */
    public boolean isUsernameExisted(String username) {
        return userRepo.countByUsername(username) > 0;
    }

    /**
     * check Email is exist
     * @param Email
     * @return
     */
    public boolean isEmailExisted(String Email) {
        return userRepo.countByEmail(Email) > 0;
    }

    /**
     * check Mobile is exist
     * @param Mobile
     * @return
     */
    public boolean isMobileExisted(String Mobile) {
        return userRepo.countByMobile(Mobile) > 0;
    }
}
