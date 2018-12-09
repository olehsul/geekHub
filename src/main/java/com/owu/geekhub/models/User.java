package com.owu.geekhub.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    private Long id;
    // todo: fix username & email
    @Column(unique = true)
    private String username;
    private String password;
    private String lastName;
    private String firstName;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int cityId;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthDate;
    private int activationKey;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean active;
    private boolean activated;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

//    @OneToMany(targetEntity = Message.class)
//    @ToString.Exclude private Set<Message> messages = new HashSet<>();

    public User() {}

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
//    INSERT INTO user(id, account_non_expired,
//                     account_non_locked, activated,
//                     activation_key, active, birth_date,
//                     city_id, credentials_non_expired,
//                     enabled, first_name, gender,
//                     last_name, password,
//                     role, username) values
//                                                    (71325788, true ,true, true, 43565, false , '2000-02-02', 0, true,
//                                                            true, 'Nathan', 'MALE', 'Drake', '$2a10$jsdgsdgjhdgjkgherghjekrgehj', 'ROLE_USER',
//                                                            'unchartedMail@gmail.com'),
//                                                            (80455788, true ,true, true, 43565, false , '2000-02-02', 0, true,
//                                                            true, 'Ozzy', 'MALE', 'Osbourne', '$2a10$jsdgsdgjhdgjkgherghjekrgehj', 'ROLE_USER',
//                                                            'rockStar213@gmail.com'),
//                                                            (18432348, true ,true, true, 43565, false , '2000-02-02', 0, true,
//                                                            true, 'Oscar', 'MALE', 'Wilde', '$2a10$jsdgsdgjhdgjkgherghjekrgehj', 'ROLE_USER',
//                                                            'DorianD19@gmail.com'),
//                                                            (19435780, true ,true, true, 43565, false , '2000-02-02', 0, true,
//                                                            true, 'Forest', 'MALE', 'Gump', '$2a10$jsdgsdgjhdgjkgherghjekrgehj', 'ROLE_USER',
//                                                            'RunForestRun@gmail.com'),
//                                                            (78435784, true ,true, true, 43565, false , '2000-02-02', 0, true,
//                                                            true, 'Tony', 'MALE', 'Montana', '$2a10$jsdgsdgjhdgjkgherghjekrgehj', 'ROLE_USER',
//                                                            'TonyGangstar@gmail.com'),
//                                                            (41235788, true ,true, true, 43565, false , '2000-02-02', 0, true,
//                                                            true, 'Rob', 'MALE', 'Stark', '$2a10$jsdgsdgjhdgjkgherghjekrgehj', 'ROLE_USER',
//                                                            'RobStarkNorth@gmail.com');

