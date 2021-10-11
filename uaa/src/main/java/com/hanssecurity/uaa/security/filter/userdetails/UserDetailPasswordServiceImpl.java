package com.hanssecurity.uaa.security.filter.userdetails;

import com.hanssecurity.uaa.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

/**
 * @author hans
 */
@RequiredArgsConstructor
@Service
public class UserDetailPasswordServiceImpl implements UserDetailsPasswordService{

    private final UserRepo userRepo;

    @Override
    // user->userDetails present current user, new password hash
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        return userRepo.findOptionalByUsername(userDetails.getUsername())
                .map(user ->  (UserDetails) userRepo.save(user.withPassword(newPassword)))
                .orElse(userDetails);
    }
}
