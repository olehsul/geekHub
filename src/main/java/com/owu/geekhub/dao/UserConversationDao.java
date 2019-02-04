package com.owu.geekhub.dao;

import com.owu.geekhub.models.UserConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConversationDao extends JpaRepository<UserConversation, Long> {

    @Query("select uc from UserConversation uc where uc.user_id=:id")
    List<UserConversation> findAllByUser_id(@Param("id") Long id);

    @Query("select count(uc) from UserConversation uc where uc.user_id=:uid or uc.user_id=:fid")
    Long checkIfExists(@Param("uid") Long userId, @Param("fid") Long friendId);

}
