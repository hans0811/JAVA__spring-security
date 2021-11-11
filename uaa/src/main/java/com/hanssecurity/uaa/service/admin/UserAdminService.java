package com.hanssecurity.uaa.service.admin;

import com.hanssecurity.uaa.annotaion.RoleAdminOrCreate;
import com.hanssecurity.uaa.annotaion.RoleAdminOrRead;
import com.hanssecurity.uaa.domain.User;
import com.hanssecurity.uaa.domain.dto.CreateUserDto;
import com.hanssecurity.uaa.repository.RoleRepo;
import com.hanssecurity.uaa.repository.UserRepo;
import com.hanssecurity.uaa.service.EmailService;
import com.hanssecurity.uaa.util.CryptoUtil;
import com.hanssecurity.uaa.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.querydsl.core.types.Predicate;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

import static com.hanssecurity.uaa.config.Constants.ROLE_STAFF;

/**
 * @author hans
 */

@RequiredArgsConstructor
@Service
public class UserAdminService {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final TotpUtil totpUtil;

    private final CryptoUtil cryptoUtil;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;


    /**
     * 取得全部用户列表
     *
     * @return 全部用户列表
     */
    @RoleAdminOrRead
    public Page<User> findAll(Predicate predicate, Pageable pageable){
        return userRepo.findAll(predicate, pageable);
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    @RoleAdminOrRead
    public Optional<User> findByUsername(String username) {
        return userRepo.findOptionalByUsername(username);
    }

    /**
     * 管理员直接创建用户
     *
     * @param createUserDto 用户创建必须的字段
     * @return 创建的用户
     */
    @RoleAdminOrCreate
    @Transactional
    public User createUser(final CreateUserDto createUserDto){

        return roleRepo.findOptionalByRoleName(ROLE_STAFF).map(role -> {
            // 生成默认密码
            val password = cryptoUtil.buildDefaultPassword();
            val user = userRepo.save(User.builder()
                    .username(createUserDto.getUsername())
                    .email(createUserDto.getEmail())
                    .mobile(createUserDto.getMobile())
                    .name(createUserDto.getName())
                    .usingMfa(createUserDto.isUsingMfa())
                    .mfaKey(totpUtil.encodeKeyToString())
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singleton(role))
                    .build());
            // 通过 email 发送用户密码
            emailService.send(createUserDto.getEmail(), password);
            return user;
        })
                .orElseThrow();

    }

}
