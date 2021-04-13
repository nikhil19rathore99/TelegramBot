package com.example.controller;

import com.example.handler.TelegramBotHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1")
public class ServerController {

    @Autowired
    private TelegramBotHandler telegramBotHandler;

    @Value("${imageStoreDir}")
    private static String UPLOADED_FOLDER = "/Users/jarvis/Documents/";

    @PostMapping("/upload")
    public boolean upload(@RequestParam("image")MultipartFile file){
        if(file.isEmpty()) return false;
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            telegramBotHandler.serverData.add(UPLOADED_FOLDER + file.getOriginalFilename());
            telegramBotHandler.sendServerData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
