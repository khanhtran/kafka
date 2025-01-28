package com.example.kafkademo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Value("${application.topic}")
    private String topic;

    KafkaTemplate kafkaTemplate;

    public MessageService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, String>>  sendMessage(String message) {
        return this.sendMessage(message, this.topic);
    }

    public CompletableFuture<SendResult<String, String>> sendMessage(String message, String topic) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.whenComplete(this::handleSendResult);
        return future;
    }


    private void handleSendResult(SendResult<String, String> result, Throwable ex) {
        if (ex == null) {
            logger.info("Send success {}", result);
            return;
        }
        logger.error("Send failure {}", result, ex);
    }


}
