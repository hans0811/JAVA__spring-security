package com.hanssecurity.uaa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanssecurity.uaa.security.auth.ldap.LDAPMultiAuthenticationProvider;
import com.hanssecurity.uaa.security.filter.RestAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author hans
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@Order(100)
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final LDAPMultiAuthenticationProvider ldapMultiAuthenticationProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin(login -> login
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .defaultSuccessUrl("/")
//                .failureHandler(jsonLoginFailureHandler())
//                .successHandler(new UaaSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                                .logoutUrl("/perform_logout")
                                .logoutSuccessUrl("/login")
//                .logoutSuccessHandler(jsonLogoutSuccessHandler())
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("someSecret")
                        .tokenValiditySeconds(86400))
                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // set 2 providers
        auth.authenticationProvider(daoAuthenticationProvider);
        auth.authenticationProvider(ldapMultiAuthenticationProvider);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(jsonLoginSuccessHandler());
        filter.setAuthenticationFailureHandler(jsonLoginFailureHandler());
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/authorize/login");
        return filter;
    }

    private LogoutSuccessHandler jsonLogoutSuccessHandler() {
        return (req, res, auth) -> {
            if (auth != null && auth.getDetails() != null) {
                req.getSession().invalidate();
            }
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().println();
            log.debug("成功退出登录");
        };
    }

    private AuthenticationSuccessHandler jsonLoginSuccessHandler() {
        return (req, res, auth) -> {
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().println();
            log.debug("认证成功");
        };
    }

    private AuthenticationFailureHandler jsonLoginFailureHandler() {
        return (req, res, exp) -> {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            val errData = Map.of(
                    "title", "认证失败",
                    "details", exp.getMessage()
            );
            res.getWriter().println(objectMapper.writeValueAsString(errData));
        };
    }
}
