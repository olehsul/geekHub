package com.owu.geekhub.service;

import com.owu.geekhub.models.UserSearchModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserSearchService {
    List<UserSearchModel> findUser(String fullName);
}
