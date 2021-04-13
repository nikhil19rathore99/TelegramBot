package com.example;

import com.example.handler.TelegramBotConfig;
import com.example.handler.TelegramBotHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication(scanBasePackages = {"com.example"})
public class TelegramBotApplication {
    public static void main(String[] args) {
        System.out.println("Bot Started!!");
        new TelegramBotConfig().start();
        SpringApplication.run(TelegramBotApplication.class, args);
    }
}
