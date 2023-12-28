package com.skillbox.cryptobot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Table(name = "subscriber")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;

    @Column(name = "chat_id", unique = true)
    private Long chatId;

    private Double price;

    @Column(name = "notificated_at")
    private Instant notificatedAt;
}
