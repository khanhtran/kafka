package com.example.kafkademo;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "${application.topic}", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@DirtiesContext
class MessageServiceIT {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private MessageService messageService;

    @Value("${application.topic}")
    private String topic;
    @Value("${application.group}")
    private String group;

    @Test
    void testSendMessage() {
        // Set up Kafka consumer properties
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(group, "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Create a Kafka consumer
        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps)
                .createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topic);

        // Send a message using the MessageService
        String testMessage = "Hello, Kafka!";
        messageService.sendMessage(testMessage);

        // Retrieve and verify the message from Kafka
        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, topic);

        assertThat(record.value()).isEqualTo(testMessage);
    }
}

