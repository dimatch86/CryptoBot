package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class NotificationService {

    @Value("${scheduler.fixedDelay}")
    private int delay;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final SubscriberService subscriberService;
    private final CryptoBot cryptoBot;


    @Scheduled(fixedRateString = "${scheduler.fixedRate}")
    public void notifySubscribers() throws IOException {

        double currentPrice = cryptoCurrencyService.getBitcoinPrice();

        List<Subscriber> subscribers = subscriberService
                .getSubscribersByPriceAndNotificationTime(currentPrice, Instant.now().minus(delay, ChronoUnit.MINUTES));
        subscribers.forEach(subscriber -> {
            cryptoBot.sendMessageToSubscriber(subscriber.getChatId(),
                    MessageFormatter.format("Пора покупать, стоимость биткоина {} USD",
                            TextUtil.toString(currentPrice)).getMessage());
            subscriberService.updateNotificationTime(subscriber.getId());
        });
    }
}
