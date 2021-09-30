package com.hanssecurity.uaa.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static java.util.stream.Collectors.toList;

/**
 * @author hans
 */
@Component
public class JwtUtil {

    // for sign
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String createJwtToken(UserDetails userDetails){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("mooc")
                .claim("authorities", userDetails.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .collect(toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 60_000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
