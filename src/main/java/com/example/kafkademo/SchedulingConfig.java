package com.example.kafkademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;

@Configuration

@EnableScheduling
@Profile("!integrationTest")
public class SchedulingConfig {
    @Autowired
    private MessageService messageService;

    @Scheduled(fixedRate = 10000)
    public void send() {
        messageService.sendMessage(LocalTime.now().toString());
    }
}
