package com.example.exchangerate.bot;

import com.example.exchangerate.model.UsersChat;
import com.example.exchangerate.services.UsersChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UpdateReceiver {
    @Autowired
    private UsersChatService usersChatService;
    private static final Map<Long, BotState> chatState = new HashMap<>();
    public SendMessage handleUpdates(Update update) {
        if(chatState.isEmpty() || !chatState.containsKey(getChatId(update))) {
            saveNewUser(update);
            chatState.put(getChatId(update), BotState.START);
        }
        if (update.hasCallbackQuery()){
            return CallBackQueryHandler.handleCallBackQuery(update, chatState.get(getChatId(update)));
        }
        if (update.hasMessage() && update.getMessage().hasText()){
            if(update.getMessage().getText().equals("/start")) {
                changeBotState(getChatId(update), BotState.ASK_CURRENCY);
                return new SendMessage().setChatId(getChatId(update)).setReplyMarkup(CallBackQueryHandler.getInlineKeyboardMarkupCurrency())
                            .setText(CallBackQueryHandler.getStateText(getUSerName(update), chatState.get(getChatId(update))));
            }
            else return new SendMessage().setChatId(getChatId(update)).setText("Пожалуйста, введите команду 'start' для начала работы или выберите один из представленных вариантов");

        }
        else return new SendMessage().setChatId(getChatId(update)).setText("Пожалуйста, введите команду 'start' для начала работы или выберите один из представленных вариантов");
    }
    public static void changeBotState(long chatId, BotState botState){
        chatState.replace(chatId, botState);
    }
    public static long getChatId(Update update){
        if(update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChatId();
        else
            return update.getMessage().getChatId();
    }
    public static String getUSerName(Update update){
        if(update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChat().getFirstName();
        else
            return update.getMessage().getChat().getFirstName();
    }
    public void saveNewUser(Update update){
        Optional<UsersChat> optionalUsersChat = usersChatService.getByChatId(getChatId(update));
        if (optionalUsersChat.isEmpty()) {
            usersChatService.create(new UsersChat(update.getMessage().getChatId(),
                    update.getMessage().getChat().getUserName(),
                    update.getMessage().getChat().getFirstName(),
                    update.getMessage().getChat().getLastName()));
        }
    }
}
