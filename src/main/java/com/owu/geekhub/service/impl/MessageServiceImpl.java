package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.models.Message;
import com.owu.geekhub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDAO messageDAO;

    @Override
    public void save(Message msg) {

    }
}
