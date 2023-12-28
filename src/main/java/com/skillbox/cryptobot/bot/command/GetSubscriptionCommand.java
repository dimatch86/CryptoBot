package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.exception.SubscriberNotFoundException;
import com.skillbox.cryptobot.service.SubscriberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final SubscriberService subscriberService;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long id = message.getChat().getId();
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            Double price = subscriberService.getSubscription(id);
            String text = (price == null) ? "Активные подписки отсутствуют" :
                    MessageFormatter.format("Вы подписаны на стоимость биткоина {} USD", price).getMessage();
            answer.setText(text);
        } catch (SubscriberNotFoundException e) {
            answer.setText("Начните работу с кнопки \"Start\"");
        } finally {
            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                log.error("Error occurred in /get_subscription command", e);
            }
        }
    }
}