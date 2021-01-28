package com.example.exchangerate.bot;

import com.example.exchangerate.model.UsersChat;
import com.example.exchangerate.services.UsersChatService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class UpdateReceiver {
    @Autowired
    private UsersChatService usersChatService;
    //private static Logger logger = (Logger) LoggerFactory.getLogger(UpdateReceiver.class);
    private static final Map<Long, BotState> chatState = new HashMap<>();
    public SendMessage handleUpdates(Update update) {
        if(chatState.isEmpty() || !chatState.containsKey(getChatId(update))) {
            saveNewUser(update);
            chatState.put(getChatId(update), BotState.START);
            log.info("User "+ getUserAdres(update) + " started working with bot");
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
    public static String getUserAdres(Update update){
        if(update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChat().getUserName();
        else
            return update.getMessage().getChat().getUserName();
    }

    public void saveNewUser(Update update){
        Optional<UsersChat> optionalUsersChat = usersChatService.getByChatId(getChatId(update));
        if (optionalUsersChat.isEmpty()) {
            UsersChat usersChat = new UsersChat();
            usersChat.setChatId(update.getMessage().getChatId());
            usersChat.setUserName(update.getMessage().getChat().getUserName());
            usersChat.setFirstName(update.getMessage().getChat().getFirstName());
            usersChat.setLastName(update.getMessage().getChat().getLastName());
            usersChatService.create(usersChat);

            log.info("User "+ usersChat + " beeing added to database");
        }
    }
}
