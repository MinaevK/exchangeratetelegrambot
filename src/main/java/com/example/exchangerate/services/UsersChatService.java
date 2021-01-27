package com.example.exchangerate.services;

import com.example.exchangerate.model.UsersChat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UsersChatService {
    List<UsersChat> getAll();
    Optional<UsersChat> getByChatId(long chatId);
    UsersChat create(UsersChat usersChat);

}
