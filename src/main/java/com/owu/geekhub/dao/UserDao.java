package com.owu.geekhub.dao;

import com.owu.geekhub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String s);
    boolean existsDistinctByUsername(String username);
    boolean existsDistinctById(Long id);
    List<User> findAllByFirstNameContainsAndLastNameContains(String name, String surname);
    @Transactional
    void deleteUserById(Long id);
    boolean existsByUsername(String username);
}
