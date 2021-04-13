package com.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class TelegramBotConfig {
    public void start() {
        try {
            log.info("Instantiate Telegram Bots API...");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            log.info("Register Telegram Bots API...");
            botsApi.registerBot(new TelegramBotHandler());
        } catch (TelegramApiException e) {
            log.error("Exception instantiate Telegram Bot!", e);
        }
    }
}
