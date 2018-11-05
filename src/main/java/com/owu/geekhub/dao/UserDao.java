package com.owu.geekhub.dao;

import com.owu.geekhub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
}
