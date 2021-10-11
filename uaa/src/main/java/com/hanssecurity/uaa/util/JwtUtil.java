package com.hanssecurity.uaa.util;

import com.hanssecurity.uaa.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

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

    public String createAccessTokenWithRefreshToken(String token) {
        return parseClaims(token, refreshKey)
                .map(claims->Jwts.builder()
                        .setClaims(claims)
                        .setExpiration(new Date(System.currentTimeMillis()
                                            + appProperties.getJwt().getAccessTokenExpireTime()))
                        .setIssuedAt(new Date())
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact()
                )
                .orElseThrow(() -> new AccessDeniedException("Visit deny"));
    }

    private Optional<Claims> parseClaims(String token, Key key) {
        try{
            val claims = Jwts.parserBuilder()
                                        .setSigningKey(key)
                                        .build()
                                        .parseClaimsJws(token)
                                        .getBody();
            return Optional.of(claims);

        }catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean validateAccessTokenWithoutExpiration(String token) {
        return validateToken(token, key, false);
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, key, true);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey, true);
    }

    public boolean validateToken(String token, Key key, boolean isExpiredInvalid){

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return true;
        }catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){

            if(e instanceof ExpiredJwtException){
                return !isExpiredInvalid;
            }
            return false;
        }
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
