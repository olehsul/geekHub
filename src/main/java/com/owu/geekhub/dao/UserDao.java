package com.owu.geekhub.dao;

import com.owu.geekhub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    UserDetails findByUsername(String s);
    UserDetails findByEmail(String email);
}
