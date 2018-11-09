package com.owu.geekhub.dao;

import com.owu.geekhub.models.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentityDao extends JpaRepository<UserIdentity, Long> {
}
