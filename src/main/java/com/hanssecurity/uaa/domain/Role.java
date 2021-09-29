package com.hanssecurity.uaa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


/**
 * @author hans
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "mooc_roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private String authority;
}
