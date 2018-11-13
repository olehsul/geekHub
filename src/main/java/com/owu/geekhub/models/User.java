package com.owu.geekhub.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // todo: fix username & email
    @Column(unique = true)
    private String username;
    private String password;
    private String lastName;
    private String firstName;

//    @OneToOne(cascade = CascadeType.ALL)
//    private UserIdentity identity;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int cityId;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthDate;
    private int activationKey;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean Activated;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

//    public User(String email, String password, String firstName, String lastName, int genderId, int cityId, Date birthDate) {
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.genderId = genderId;
//        this.cityId = cityId;
//        this.birthDate = birthDate;
//    }
//
//    public User(String email, String password, String firstName, String lastName, Date birthDate) {
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.birthDate = birthDate;
//    }
//
//    public User(String email, String password, String firstName, String lastName) {
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }
}
