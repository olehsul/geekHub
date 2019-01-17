package com.owu.geekhub.dao;

import com.owu.geekhub.models.FriendshipRequest;
import com.owu.geekhub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRequestDAO extends JpaRepository<FriendshipRequest, Long> {
    FriendshipRequest findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);
    List<FriendshipRequest> findAllBySender_Id(Long senderId);
    List<FriendshipRequest> findAllBySender(User sender);
    List<FriendshipRequest> findAllByReceiver_Id(Long senderId);
}
