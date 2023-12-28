package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.exception.SubscriberNotFoundException;
import com.skillbox.cryptobot.service.SubscriberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    private final SubscriberService subscriberService;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long id = message.getChat().getId();
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            subscriberService.unsubscribe(id);
            answer.setText("Подписка отменена");
        } catch (SubscriberNotFoundException e) {
            answer.setText("Начните работу с кнопки \"Start\"");
        } finally {
            try {
                absSender.execute(answer);
            } catch (Exception e) {
                log.error("Ошибка возникла в /unsubscribe методе", e);
            }
        }
    }
}