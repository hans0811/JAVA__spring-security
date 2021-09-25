package com.hanssecurity.uaa.rest;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author hans
 */

// http://localhost:8080/api/greeting
@RestController
@RequestMapping("/api")
public class UserResource {

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

    @Data
    static class Profile {
        private String gender;
        private String idNo;

    }


}
