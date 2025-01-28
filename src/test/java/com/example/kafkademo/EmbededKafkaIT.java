package com.example.kafkademo;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org. springframework. kafka. core. DefaultKafkaConsumerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.time.Duration;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1,
        topics = "my-test-topic",
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@DirtiesContext
@ActiveProfiles("integrationTest")
public class EmbededKafkaIT {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;


    private String topic = "my-test-topic";

    @Test
    public void testKafkaIntegration() throws ExecutionException, InterruptedException {
        // Send a message

        kafkaTemplate.send(topic, "test-message").get();



        // Consume the message
        Map<String, Object> consumerProps = new HashMap<>(consumerFactory.getConfigurationProperties());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        ConsumerFactory<String, String> customConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);

        Consumer<String, String> consumer = customConsumerFactory.createConsumer();
        consumer.subscribe(Collections.singleton(topic+"aaa"));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(30));
        assertThat(records.count()).isEqualTo(1);
        assertThat(records.iterator().next().value()).isEqualTo("test-message");
    }
}
