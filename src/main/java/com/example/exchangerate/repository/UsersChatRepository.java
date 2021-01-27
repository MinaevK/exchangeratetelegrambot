package com.example.exchangerate.repository;

import com.example.exchangerate.model.UsersChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersChatRepository extends JpaRepository<UsersChat, Long> {
    UsersChat findByChatId(long chatId);
}
