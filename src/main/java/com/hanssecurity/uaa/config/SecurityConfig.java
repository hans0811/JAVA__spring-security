package com.hanssecurity.uaa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanssecurity.uaa.security.filter.RestAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author hans
 */
@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity(debug = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ObjectMapper objectMapper;
    private final SecurityProblemSupport securityProblemSupport;

    // rewrite configure
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //super.configure(http);
//        http
//                .formLogin(Customizer.withDefaults())
//                .authorizeRequests(req -> req.mvcMatchers("/api/greeting").authenticated());
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http
//                .authorizeRequests()
//                .antMatchers("/api/**").hasRole("USER")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/login").usernameParameter("username1")
//                .and()
//                .httpBasic().realmName("BA");

        http
                .exceptionHandling(exp-> exp
                        .authenticationEntryPoint(securityProblemSupport)
                        .accessDeniedHandler(securityProblemSupport))
                .authorizeRequests(req -> req
                        .antMatchers("/authorize/**").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/api/**").hasRole("USER")
                        .anyRequest().authenticated())
                        .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                        .csrf(crsf -> crsf.ignoringAntMatchers("/authorize/**", "/admin/**", "/api/**"));

                //.formLogin(AbstractHttpConfigurer::disable)
//                .formLogin(form -> form.loginPage("/login")
//                            .usernameParameter("username1")
//                            //.loginProcessingUrl("")
//                            .successHandler(jsonAuthenticationSuccessHandler())
//                            .failureHandler(jsonLoginFailureHandler())
//                            .permitAll())
//                            //.httpBasic(withDefaults())
//                            .csrf(withDefaults())
//                        //.csrf(AbstractHttpConfigurer::disable);
//                        .logout(longUrl -> longUrl.logoutUrl("/perform_logout")
//                                .logoutSuccessHandler(((req, res, auth) -> {
//                                    val objMapper = new ObjectMapper();
//                                    res.setStatus(HttpStatus.OK.value());
//                                    res.getWriter().println(objMapper.writeValueAsString(auth));
//                                    log.debug("logout successfully");
//
//                                })))
//                        .rememberMe(rememberMe -> rememberMe.tokenValiditySeconds(100).rememberMeCookieName("someKeyToRemember"));


//        http.csrf(csrf -> csrf.disable() )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(form -> form.disable())
//                .authorizeRequests(req -> req.antMatchers("/api/**").authenticated());
    }

    private AuthenticationFailureHandler jsonLoginFailureHandler() {
        return (req, res, exp) -> {
            val objMapper = new ObjectMapper();
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            // final Map<String, String>
            val errData = Map.of(
                    "title", "Authentication failed!!!!",
                    "details", exp.getMessage()
            );
            res.getWriter().println(objMapper.writeValueAsString(errData));

        };
    }

    private RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(jsonLoginFailureHandler());
        // authenticationManager from parent
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/authorize/login");
        return filter;
    }

    private AuthenticationSuccessHandler jsonAuthenticationSuccessHandler() {
        return (req, res, auth) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().println(objectMapper.writeValueAsString(auth));
            log.debug("success");
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("12345678"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("TestOldEncode")
                .password(new MessageDigestPasswordEncoder("SHA-1").encode("abcd1234"))
                .roles("USER", "ADMIN");
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        // support and adopt new encoder
        val idForDefault = "bcrypt";

        val encoders = Map.of(
             idForDefault, new BCryptPasswordEncoder(),
                "SHA-1", new MessageDigestPasswordEncoder("SHA-1")
        );

        return new DelegatingPasswordEncoder(idForDefault, encoders);

        // Pbkdf2PasswordEncoder()
        //return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/public/**", "/error")
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
