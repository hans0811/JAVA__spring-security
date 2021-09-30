package com.hanssecurity.uaa.util;

import com.hanssecurity.uaa.config.AppProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static java.util.stream.Collectors.toList;

/**
 * @author hans
 */
@RequiredArgsConstructor
@Component
public class JwtUtil {

    // for visiting token sign
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // for refreshing token sign
    public static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final AppProperties appProperties;


    // create help

    public String createAccessToken(UserDetails userDetails) {
        return createJwtToken(userDetails, appProperties.getJwt().getAccessTokenExpireTime(),key);
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails, appProperties.getJwt().getRefreshTokenExpireTime(),refreshKey);
    }

    public String createJwtToken(UserDetails userDetails, long timeToExpire, Key key){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("mooc")
                .claim("authorities", userDetails.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .collect(toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + timeToExpire))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
