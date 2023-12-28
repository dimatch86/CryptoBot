package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.exception.SubscriberNotFoundException;
import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    @Transactional
    public void saveSubscriber(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }

    @Transactional
    public void subscribe(long id, Double price) {
        Subscriber subscriber = getSubscriber(id);
        subscriber.setPrice(price);
        subscriberRepository.save(subscriber);
    }

    public Double getSubscription(long id) {
        Subscriber subscriber = getSubscriber(id);
        return subscriber.getPrice();
    }

    @Transactional
    public void unsubscribe(long id) {
        Subscriber subscriber = getSubscriber(id);
        subscriber.setPrice(null);
        subscriberRepository.save(subscriber);
    }

    public List<Subscriber> getSubscribersByPriceAndNotificationTime(Double price, Instant instant) {
        return subscriberRepository
                .findAllByPriceGreaterThanEqualAndNotificatedAtLessThanEqual(price, instant);
    }

    @Transactional
    public void updateNotificationTime(long id) {
        Subscriber subscriber = getSubscriber(id);
        subscriber.setNotificatedAt(Instant.now());
        subscriberRepository.save(subscriber);
    }

    private Subscriber getSubscriber(long id) {
        return subscriberRepository.findByChatId(id)
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found"));
    }

}
