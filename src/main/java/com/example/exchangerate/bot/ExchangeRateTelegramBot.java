package com.example.exchangerate.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Properties;

@Slf4j
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
        return "null";
    }
}
