package com.hanssecurity.uaa.security.auth.ldap;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.ldap.DataLdapTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hans
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@DataLdapTest
class LDAPUserRepoTest {
    @Autowired
    LDAPUserRepo ldapUserRepo;

    @Test
    public void givenUsernameAndPassword_ThenFIndUserSuccess(){
        String username = "zhaoliu";
        String password = "123";

        val user = ldapUserRepo.findByUsernameAndPassword(username, password);
        assertTrue(user.isPresent());
    }

    @Test
    public void givenUsernameAndWrongPassword_ThenFIndUserFailed(){
        String username = "zhaoliu";
        String password = "wrong";

        val user = ldapUserRepo.findByUsernameAndPassword(username, password);
        assertTrue(user.isEmpty());

    }


}