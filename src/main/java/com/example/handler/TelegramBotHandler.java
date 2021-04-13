package com.example.handler;

import com.example.services.CommonThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class TelegramBotHandler extends TelegramLongPollingBot {

    private HashMap<Long,UUID> userMap;
    private ExecutorService threadPool;
    public Queue<String> serverData;

    public String chatId;

    @Autowired
    private CommonThreadPoolService commonThreadPoolService;

    @PostConstruct
    public void init(){
        System.out.println("Init called");
        userMap = new HashMap<>();
        serverData = new LinkedList<>();
        threadPool = commonThreadPoolService.getExecutorService();
    }

    @Override
    public String getBotUsername() {
        return "YOUR BOT USERNAME";
    }

    @Override
    public String getBotToken() {
        return "YOUR BOT TOKEN";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.toString());
        if(userMap==null) userMap =new HashMap<>();
        else if(update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String userCommand = message.getText();
            long userId = message.getFrom().getId();
            if (!userMap.containsKey(userId)) {
                userMap.put(userId, UUID.randomUUID());
            }

            SendMessage sendMessage = new SendMessage();
            if (userCommand.equals("/myid")) {
                sendMessage.setText("Hello " + message.getFrom().getUserName() + "!! \nPlease paste this id to our app: " + userMap.get(userId).toString());
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                this.chatId = String.valueOf(message.getChatId());
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            sendServerData();
        }
    }

    public void sendServerData(){
        if(chatId!=null){
            threadPool.execute(() -> {
                while(!serverData.isEmpty()){
                       String uri = serverData.poll();
                       if(uri.endsWith(".mp3")){
                           SendAudio sendAudio = new SendAudio();
                           sendAudio.setChatId(chatId);
                           sendAudio.setAudio(new InputFile(new File(uri)));
                           try {
                               execute(sendAudio);
                               Thread.sleep(5000);
                           } catch (TelegramApiException | InterruptedException e) {
                               e.printStackTrace();
                           }
                       }else {
                           SendPhoto sendPhoto = new SendPhoto();
                           sendPhoto.setChatId(chatId);
                           sendPhoto.setPhoto(new InputFile(new File(uri)));
                           try {
                               execute(sendPhoto);
                               Thread.sleep(5000);
                           } catch (TelegramApiException | InterruptedException e) {
                               e.printStackTrace();
                           }
                       }
                }
            });
        }
    }
}
