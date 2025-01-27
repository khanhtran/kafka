package com.example.kafkademo;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private MessageService messageService;

    @Test
    void testSendMessage_Success() throws Exception {
        // Arrange: Mock the behavior of KafkaTemplate to return a CompletableFuture
        SendResult<String, String> sendResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, String>> completableFuture = CompletableFuture.completedFuture(sendResult);

        when(kafkaTemplate.send(any(String.class), any(String.class))).thenReturn(completableFuture);

        // Act: Call the method
        messageService.sendMessage("Test message", "testTopic");

        // Assert: Verify that send was called on kafkaTemplate
        verify(kafkaTemplate, times(1)).send(any(String.class), any(String.class));
    }

    @Test
    void testSendMessage_Failure() throws Exception {
        // Arrange: Mock the behavior of KafkaTemplate to throw an exception
        CompletableFuture<SendResult<String, String>> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(new RuntimeException("Kafka failure"));

        when(kafkaTemplate.send(any(String.class), any(String.class))).thenReturn(completableFuture);

        // Act: Call the method
        messageService.sendMessage("Test message", "testTopic");

        // Assert: Verify that send was called on kafkaTemplate
        verify(kafkaTemplate, times(1)).send(any(String.class), any(String.class));
    }
}

