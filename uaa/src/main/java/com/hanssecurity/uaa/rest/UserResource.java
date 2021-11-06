package com.hanssecurity.uaa.rest;

import com.hanssecurity.uaa.domain.User;
import com.hanssecurity.uaa.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author hans
 */

// http://localhost:8080/api/greeting
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;

    @GetMapping("/greeting")
    public String greeting(){
        return "Hello world";
    }

    @PostMapping("/greeting")
    @ResponseStatus(HttpStatus.CREATED)
    public String makeGreeting(@RequestParam String name, @RequestBody Profile profile){
        return "Hello world, " + name + "!\n" + profile.gender;
    }

    @PutMapping("/greeting/{name}")
    public String putGreeting(@PathVariable String name){
        return "Hello world, " + name;
    }

//    @GetMapping("/principal")
//    public Authentication getPrincipal() {
//        return SecurityContextHolder.getContext().getAuthentication();
//    }

//    @GetMapping("/principal")
//    public Authentication getPrincipal(Authentication authentication) {
//        return authentication;
//    }

    @GetMapping("/principal")
    public Principal getPrincipal(Principal principal) {
        return principal;
    }

    @GetMapping("/users/{username}")
    public String getCurrentUsername(@PathVariable String username){
        return "Hello, " + username;
    }


    @GetMapping("/users/manager")
    public String getManagerResource(){
        return "hi,";
    }


    //@PreAuthorize("hasRole('ADMIN')")
    @PostAuthorize("returnObject.username == authentication.principal.username")
    @GetMapping("/users/by-email/{email}")
    public User getUserByEmail(String email) {
        return userService.findOptionalByEmail(email).orElseThrow();
    }

    @Data
    static class Profile {
        private String gender;
        private String idNo;

    }


}
