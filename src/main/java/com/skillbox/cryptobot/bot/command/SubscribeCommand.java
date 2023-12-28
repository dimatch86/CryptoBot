package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.exception.IncorrectInputException;
import com.skillbox.cryptobot.bot.exception.SubscriberNotFoundException;
import com.skillbox.cryptobot.service.SubscriberService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SubscribeCommand implements IBotCommand {

    private final SubscriberService subscriberService;
    private final GetPriceCommand getPriceCommand;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long id = message.getChat().getId();
        String text = message.getText();

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            double price = Double.parseDouble(getStringPrice(text));
            subscriberService.subscribe(id, price);
            answer.setText(
                    MessageFormatter.format("Новая подписка создана на стоимость {} USD", TextUtil.toString(price)).getMessage());
            getPriceCommand.processMessage(absSender, message, arguments);
        } catch (SubscriberNotFoundException e) {
            answer.setText("Начните работу с кнопки \"Start\"");
        } catch (NumberFormatException | IncorrectInputException e) {
            answer.setText("Некорректная команда. Повторите ввод команды в формате \"/subscribe [число]\"");
        } finally {
            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                log.error("Ошибка возникла /subscribe методе", e);
            }
        }
    }

    private String getStringPrice(String text) {
        String[] words = text.split("\s+");
        if (words.length != 2) {
            throw new IncorrectInputException("Некорректный ввод данных");
        }
        return words[1];
    }
}