package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    Optional<Subscriber> findByChatId(long id);

    List<Subscriber> findAllByPriceGreaterThanEqualAndNotificatedAtLessThanEqual(Double price, Instant instant);
}
