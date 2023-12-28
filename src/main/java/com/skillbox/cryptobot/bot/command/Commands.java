package com.skillbox.cryptobot.bot.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Commands {

    START ("/start"),
    GET_PRICE ("/get_price"),
    GET_SUBSCRIPTION ("/get_subscription"),
    SUBSCRIBE ("/subscribe"),
    UNSUBSCRIBE ("/unsubscribe");

    private final String commandType;
}
