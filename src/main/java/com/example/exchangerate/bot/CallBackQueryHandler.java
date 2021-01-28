package com.example.exchangerate.bot;

import com.example.exchangerate.model.Currency;
import com.example.exchangerate.parser.WebParser;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CallBackQueryHandler {
    private static Map<Long, Currency> chosenCurrency = new HashMap<>();
    public static SendMessage handleCallBackQuery(Update update, BotState botState){
        switch (botState){
            case START:
                return new SendMessage().setChatId(UpdateReceiver.getChatId(update)).setText("Пожалуйста, введите команду 'start' для начала работы");
            case ASK_BANK:
                log.info(UpdateReceiver.getUserAdres(update) + " made a request to bank");
                return handleCallBackQueryBank(update);
            case ASK_CURRENCY:
                log.info(UpdateReceiver.getUserAdres(update) + " made a request to currency");
                return handleCallBackQueryCurrency(update);
            default:
                return new SendMessage().setChatId(update.getMessage().getChatId()).setText("Пожалуйста, выберите один из представленных вариантов");
        }
    }
    //Handle currency request
    public static SendMessage handleCallBackQueryCurrency(Update update){
        if (update.hasCallbackQuery()){
            if(chosenCurrency.containsKey(UpdateReceiver.getChatId(update)))
                chosenCurrency.remove(UpdateReceiver.getChatId(update));
            switch (update.getCallbackQuery().getData()){
                case "buttonDollar" :
                    chosenCurrency.put(UpdateReceiver.getChatId(update), Currency.USD);
                    UpdateReceiver.changeBotState(UpdateReceiver.getChatId(update), BotState.ASK_BANK);
                    return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setReplyMarkup(getInlineKeyboardMarkupBank()).setText("Выберите источник данных:");
                case "buttonEuro" :
                    chosenCurrency.put(UpdateReceiver.getChatId(update), Currency.EUR);
                    UpdateReceiver.changeBotState(UpdateReceiver.getChatId(update), BotState.ASK_BANK);
                    return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setReplyMarkup(getInlineKeyboardMarkupBank()).setText("Выберите источник данных:");
                case "buttonRuble" :
                    chosenCurrency.put(UpdateReceiver.getChatId(update), Currency.RUB);
                    UpdateReceiver.changeBotState(UpdateReceiver.getChatId(update), BotState.ASK_BANK);
                    return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setReplyMarkup(getInlineKeyboardMarkupBank()).setText("Выберите источник данных:");
                default:
                    return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("Error");
            }
        }
        else
            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("Error");
    }

    //Handle bank request
    public static SendMessage handleCallBackQueryBank(Update update){
        if (update.hasCallbackQuery()){
            switch (update.getCallbackQuery().getData()){
                case "buttonOshad" :
                    UpdateReceiver.changeBotState(UpdateReceiver.getChatId(update), BotState.START);
                    switch (chosenCurrency.get(UpdateReceiver.getChatId(update))){
                        case USD:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParceOshad().get(1).toString());
                        case EUR:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParceOshad().get(0).toString());
                        case RUB:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParceOshad().get(2).toString());
                    }
                case "buttonPrivat" :
                    UpdateReceiver.changeBotState(UpdateReceiver.getChatId(update), BotState.START);
                    switch (chosenCurrency.get(UpdateReceiver.getChatId(update))){
                        case USD:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParsePrivat().get(1).toString());
                        case EUR:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParsePrivat().get(0).toString());
                        case RUB:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParsePrivat().get(2).toString());
                    }
                case "buttonMoney" :
                    UpdateReceiver.changeBotState(UpdateReceiver.getChatId(update), BotState.START);
                    switch (chosenCurrency.get(UpdateReceiver.getChatId(update))){
                        case USD:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParseMoney().get(1).toString());
                        case EUR:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParseMoney().get(0).toString());
                        case RUB:
                            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText(WebParser.ParseMoney().get(2).toString());
                    }
                default:
                    return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("Пожалуйста, выберите один из представленных сервисов");
            }
        }
        else
            return new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("Пожалуйста, выберите один из представленных сервисов");
    }

    //Markup for currency form
    public static InlineKeyboardMarkup getInlineKeyboardMarkupCurrency(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonDollar = new InlineKeyboardButton().setText("$");
        InlineKeyboardButton buttonEuro = new InlineKeyboardButton().setText("€");
        InlineKeyboardButton buttonRuble = new InlineKeyboardButton().setText("₽");

        buttonDollar.setCallbackData("buttonDollar");
        buttonEuro.setCallbackData("buttonEuro");
        buttonRuble.setCallbackData("buttonRuble");

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(buttonDollar);
        keyboardButtonsRow.add(buttonEuro);
        keyboardButtonsRow.add(buttonRuble);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    //Markup for bank form
    public static InlineKeyboardMarkup getInlineKeyboardMarkupBank(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOshad = new InlineKeyboardButton().setText("Ощадбанк");
        InlineKeyboardButton buttonPrivat = new InlineKeyboardButton().setText("Приватбанк");
        InlineKeyboardButton buttonMoney = new InlineKeyboardButton().setText("Money24");

        buttonOshad.setCallbackData("buttonOshad");
        buttonPrivat.setCallbackData("buttonPrivat");
        buttonMoney.setCallbackData("buttonMoney");

        List<InlineKeyboardButton> keyboardButtonsRowBank = new ArrayList<>();
        keyboardButtonsRowBank.add(buttonOshad);
        keyboardButtonsRowBank.add(buttonPrivat);
        keyboardButtonsRowBank.add(buttonMoney);

        List<List<InlineKeyboardButton>> rowListBank = new ArrayList<>();
        rowListBank.add(keyboardButtonsRowBank);

        inlineKeyboardMarkup.setKeyboard(rowListBank);
        return inlineKeyboardMarkup;
    }

    public static String getStateText(String userName, BotState botState){
        switch (botState){
            case ASK_CURRENCY:
                return "Добрый день, " + userName + ". Выберите валюту, которую будем искать";
            case ASK_BANK:
                return "Выберите источник данных:";
            default:
                return "";
        }
    }
}
