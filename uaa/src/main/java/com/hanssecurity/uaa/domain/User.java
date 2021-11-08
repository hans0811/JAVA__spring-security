package com.hanssecurity.uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanssecurity.uaa.config.Constants;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hans
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Data
@Entity
@Table(name="mooc_users")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, unique = true, nullable = false)
    private String username;
    @JsonIgnore
    @Column(name = "password_hash", length = 80, unique = true)
    private String password;
    @Column(length = 255, unique = true, nullable = false)
    private String email;
    @Column(length = 11, unique = true, nullable = false)
    private String mobile;
    @Column(length = 50)
    private String name;
    @Column(nullable = false)
    private boolean enabled;
    @Column(name="account_non_expired", nullable = false)
    private boolean accountNonExpired;
    @Column(name="account_non_locked", nullable = false)
    private boolean accountNonLocked;
    @Column(name="credentials_non_Expired", nullable = false)
    private boolean credentialsNonExpired;

    /**
     * Whether use 2 steps balisation
     */
    @Builder.Default
    @Column(name = "using_mfa", nullable = false)
    private boolean usingMfa = false;

    /**
     * the key is second validation
     */
    @JsonIgnore
    @Column(name = "mfa_key", nullable = false)
    private String mfaKey;


//    join the table
    @Getter
    @JsonIgnore
    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "mooc_users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().flatMap( role -> Stream.concat(
                Stream.of(new SimpleGrantedAuthority(role.getRoleName())), role.getPermissions().stream())
        ).collect(Collectors.toList());
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }
}
