package com.example.services;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Service
public class CommonThreadPoolService {
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("bot-pool"+"thread-%d").build();
    ExecutorService executorService = Executors.newFixedThreadPool(64, threadFactory);

    public ExecutorService getExecutorService() {
        return executorService;
    }
}