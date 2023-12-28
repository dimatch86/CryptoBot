package com.skillbox.cryptobot.configuration;


import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.bot.command.Commands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.*;
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
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboardRows());

        return replyKeyboardMarkup;
    }

    @Bean
    public List<KeyboardRow> keyboardRows() {
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons()));
        return rows;
    }

    @Bean
    public List<KeyboardButton> keyboardButtons() {
        List<KeyboardButton> buttons = new ArrayList<>();
        buttons.add(new KeyboardButton(Commands.START.getCommandType()));
        buttons.add(new KeyboardButton(Commands.GET_PRICE.getCommandType()));
        buttons.add(new KeyboardButton(Commands.GET_SUBSCRIPTION.getCommandType()));
        buttons.add(new KeyboardButton(Commands.UNSUBSCRIBE.getCommandType()));
        return buttons;
    }
}
