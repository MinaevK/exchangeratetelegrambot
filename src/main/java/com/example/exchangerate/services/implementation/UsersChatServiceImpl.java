package com.example.exchangerate.services.implementation;

import com.example.exchangerate.model.UsersChat;
import com.example.exchangerate.repository.UsersChatRepository;
import com.example.exchangerate.services.UsersChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersChatServiceImpl implements UsersChatService {
    private final UsersChatRepository usersChatRepository;

    @Override
    public List<UsersChat> getAll() {
        return usersChatRepository.findAll();
    }

    @Override
    public Optional<UsersChat> getByChatId(long chatId) {
        Optional<UsersChat> usersChat =usersChatRepository.findById(chatId);
        return usersChat;
    }

    @Override
    public UsersChat create(UsersChat usersChat) {
        return usersChatRepository.save(usersChat);
    }
}
