package com.hanssecurity.uaa.util;

import com.hanssecurity.uaa.config.AppProperties;
import com.hanssecurity.uaa.domain.Role;
import com.hanssecurity.uaa.domain.User;
import io.jsonwebtoken.Jwts;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hans
 */
@ExtendWith(SpringExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        jwtUtil = new JwtUtil(new AppProperties());
    }

    @Test
    public void givenUserDetails_thenCreateTokenSuccess() {
        val username = "user";
        val authorities = Set.of(
                Role.builder().authority("ROLE_USER").build(),
                Role.builder().authority("ROLE_ADMIN").build()
        );

        val user = User.builder()
                        .username(username)
                        .authorities(authorities)
                        .build();
        // create jwt
        //val token = jwtUtil.createJwtToken(user, JwtUtil.key);
        val token = jwtUtil.createAccessToken(user);

        // resolve
        val parsedClaims = Jwts.parserBuilder()
                                                .setSigningKey(jwtUtil.key)
                                                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, parsedClaims.getSubject(), "same username ");

    }
}