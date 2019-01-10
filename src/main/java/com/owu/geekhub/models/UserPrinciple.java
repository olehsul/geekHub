package com.owu.geekhub.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserPrinciple
//        implements UserDetails
{
//
//    private Long id;
//    private String username;
//    private String password;
//    private String lastName;
//    private Strig firstName;
//    private Collection<? extends GrantedAuthority> authorities;
//
//
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }
//
//    public boolean isAccountNonExpired;
//    public boolean isAccountNonLocked;
//    public boolean isCredentialsNonExpired;
//    public boolean isEnabled;
//
//    public UserPrinciple(Long id, String firstName, String lastName,
//                         String username, String password,
//                         Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.username = username;
//        this.password = password;
//        this.authorities = authorities;
//    }
//
//    public static UserPrinciple build(User user) {
////        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
////                new SimpleGrantedAuthority(role.getName().name())
////        ).collect(Collectors.toList());
//
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(user.getRole().getName().name()));
//
//        return new UserPrinciple(
//                user.getId(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getUsername(),
//                user.getPassword(),
//                authorities
//        );
//    }

}
