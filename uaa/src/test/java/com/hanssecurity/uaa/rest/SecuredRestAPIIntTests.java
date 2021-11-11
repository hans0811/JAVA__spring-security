package com.hanssecurity.uaa.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanssecurity.uaa.common.BaseIntegrationTest;
import com.hanssecurity.uaa.config.AppProperties;
import com.hanssecurity.uaa.domain.Role;
import com.hanssecurity.uaa.domain.User;
import com.hanssecurity.uaa.domain.dto.LoginDto;
import com.hanssecurity.uaa.repository.RoleRepo;
import com.hanssecurity.uaa.repository.UserRepo;
import com.hanssecurity.uaa.util.JwtUtil;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static com.hanssecurity.uaa.config.Constants.ROLE_ADMIN;
import static com.hanssecurity.uaa.config.Constants.ROLE_USER;

import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author hans
 */
//@SpringBootTest
public class SecuredRestAPIIntTests extends BaseIntegrationTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private PasswordGenerator passwordGenerator;

    private static final String STR_KEY = "8Uy+OZUaZur9WwcP0z+YxNy+QdsWbtfqA70GQMxMfLeisTd8Na6C7DkjhJWLrGyEyBsnEmmkza6iorytQRh7OQ==";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        passwordGenerator = new PasswordGenerator();
        userRepo.deleteAllInBatch();
        roleRepo.deleteAllInBatch();
        val roleUser = Role.builder()
                //.authority(ROLE_USER)
                .build();
        val roleAdmin = Role.builder()
                //.authority(ROLE_ADMIN)
                .build();
        val savedRoleUser = roleRepo.save(roleUser);
        val savedRoleAdmin = roleRepo.save(roleAdmin);


        val user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("12345678"))
                .mobile("13012341234")
                .name("New User")
                .email("user@local.dev")
                .mfaKey(STR_KEY)
                //.authorities(Set.of(savedRoleAdmin))
                .build();
        userRepo.save(user);
    }

    @Test
    public void givenLoginDto_shouldReturnJwt() throws Exception {
        val username = "user";
        val password = "12345678";
        val loginDto = new LoginDto(username, password);
        mvc.perform(post("/authorize/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    public void givenAuthRequest_shouldSucceedWith200() throws Exception {
//        val username = "user";
//        val authorities = Set.of(
//                Role.builder()
//                        .authority(ROLE_USER)
//                        .build(),
//                Role.builder()
//                        .authority(ROLE_ADMIN)
//                        .build()
//        );
//        val user = User.builder()
//                .username(username)
//                .authorities(authorities)
//                .build();
//        val token = jwtUtil.createAccessToken(user);
//        mvc.perform(get("/api/me")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + token))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }


    //.antMatchers("/api/users/**").access("hasRole('ADMIN') or hasRole('USER')")
    @WithMockUser(username="user", roles = {"USER"})
    @Test
    public void givenRoleUserOrAdmin_thenAccessSuccess() throws Exception{
        mvc.perform(get("/api/users/{username}", "user"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user")
    @Test
    public void givenUserRole_whenQueryUserByEmail_shouldSuccess() throws Exception {
        mvc.perform(get("/api/users/by-email/{email}", "user@local.dev"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", roles = {"MANAGER"})
    @Test
    public void givenRole_whenQueryUserByEmail_shouldSuccess() throws Exception {
        mvc.perform(get("/api/users/manager"))
                .andDo(print())
                .andExpect(status().isOk());
    }



}
