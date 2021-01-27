package com.example.exchangerate.bot;

import com.example.exchangerate.model.UsersChat;
import com.example.exchangerate.parser.WebParser;
import com.example.exchangerate.services.UsersChatService;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ExchangeRateTelegramBot extends TelegramLongPollingBot {
    private String botUsername;
    private String botToken;
    Properties properties = new Properties();
    @Autowired
    UpdateReceiver updateReceiver;

    @Override
    public void onUpdateReceived(Update update) {
            try {
                execute(updateReceiver.handleUpdates(update));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    @Override
    public String getBotUsername() {
        return "exchange_rate_m_bot";
    }

    @Override
    public String getBotToken() {
        return "1638027031:AAGYFsrdrY3B4qPUOVh3qwZvSNuhf_uGnMU";
    }
}
