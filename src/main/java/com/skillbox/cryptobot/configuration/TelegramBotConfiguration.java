package com.skillbox.cryptobot.configuration;


import com.skillbox.cryptobot.bot.CryptoBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class TelegramBotConfiguration {
    @Bean
    TelegramBotsApi telegramBotsApi(CryptoBot cryptoBot) {
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(cryptoBot);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message to telegram!", e);
        }
        return botsApi;
    }

    @Bean
    public ReplyKeyboardMarkup replyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        KeyboardRow keyboardButtons1 = new KeyboardRow();
        keyboardRowList.add(keyboardButtons);
        keyboardButtons.add(new KeyboardButton("Просвяти"));
        keyboardButtons.add(new KeyboardButton("Крипта"));
        keyboardButtons.add(new KeyboardButton("Крипта1"));
        keyboardButtons1.add(new KeyboardButton("Кнопке \"начать\""));
        keyboardRowList.add(keyboardButtons1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
