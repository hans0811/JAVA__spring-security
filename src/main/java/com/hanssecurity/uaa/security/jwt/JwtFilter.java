package com.hanssecurity.uaa.security.jwt;

import com.hanssecurity.uaa.config.AppProperties;
import com.hanssecurity.uaa.util.CollectionUtils;
import com.hanssecurity.uaa.util.JwtUtil;
import com.mysql.cj.xdevapi.Collection;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * @author hans
 */
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {


    private final AppProperties appProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  get Authorization: Bearer xxxxxxx
        if(checkJwtToken(request)) {
            //
            // is empty
            validateToken(request)
                    .filter(claims -> claims.get("authorities") != null)
                    .ifPresentOrElse( this::setUpSoringAuthentication, // have value
                                      SecurityContextHolder::clearContext); // empty
        }

        filterChain.doFilter(request, response);

    }

    private void setUpSoringAuthentication(Claims claims) {
        // have value
        val rawList = CollectionUtils.convertObjectToList(claims.get("authorities"));

        // userDetails type -> Collection<? extends GrantedAuthority> getAuthorities();
        val authorities = rawList.stream()
                                            .map(String::valueOf)
                                            .map(strAuthority -> new SimpleGrantedAuthority(strAuthority))
                                            .collect(toList());
        val authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Optional<Claims> validateToken(HttpServletRequest req) {
        // replace prefix string
        String jwtToken = req.getHeader(appProperties.getJwt().getHeader().replace(appProperties.getJwt().getPrefix(), ""));
        try{
            return Optional.of(Jwts.parserBuilder().setSigningKey(JwtUtil.key).build().parseClaimsJws(jwtToken).getBody());

        }catch ( ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
            return Optional.empty();
        }
    }

    /**
     * Check JWT Token in HTTP header
     *
     * @param req
     * @return
     */
    private boolean checkJwtToken(HttpServletRequest req) {
        String authenticationHeader = req.getHeader(appProperties.getJwt().getHeader());
        return authenticationHeader != null && authenticationHeader.startsWith(appProperties.getJwt().getPrefix());
    }
}
