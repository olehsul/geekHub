package com.owu.geekhub.dao;

import com.owu.geekhub.models.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String s);
    boolean existsDistinctByUsername(String username);
    boolean existsDistinctById(Long id);
    List<User> findAllByFirstNameContainsAndLastNameContains(String name, String surname);
    
}
