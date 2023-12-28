package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


/**
 * Обработка команды начала работы с ботом
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StartCommand implements IBotCommand {

    private final ReplyKeyboardMarkup replyKeyboardMarkup;
    private final SubscriberService subscriberService;

    @Value("${scheduler.fixedDelay}")
    private int delay;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        String name = message.getChat().getFirstName();
        long id = message.getChatId();
        Subscriber subscriber = Subscriber.builder()
                .uuid(UUID.randomUUID().toString())
                .chatId(id)
                .price(null)
                .notificatedAt(Instant.now().minus(delay, ChronoUnit.MINUTES))
                .build();
        subscriberService.saveSubscriber(subscriber);


        answer.setText(MessageFormatter.format("""
                Привет, {}! Данный бот помогает отслеживать стоимость биткоина.
                Поддерживаемые команды:
                 /subscribe [число] - подписаться на стоимость битка в USD
                 /get_price - получить стоимость биткоина
                 /get_subscription - получить текущую подписку
                 /unsubscribe - отменить подписку на стоимость
                """, name).getMessage());
        answer.setReplyMarkup(replyKeyboardMarkup);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }
}