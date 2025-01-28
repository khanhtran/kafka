//package com.example.kafkademo;
//
//
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.*;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@EmbeddedKafka(partitions = 1, topics = "${application.topic}", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
//@ActiveProfiles("integrationTest")
//@DirtiesContext
//class MessageServiceIT {
//
//    @Autowired
//    private EmbeddedKafkaBroker embeddedKafkaBroker;
//
//    @Autowired
//    private MessageService messageService;
//
//    @Value("${application.topic}")
//    private String topic;
//    @Value("${application.group}")
//    private String group;
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    private ConsumerFactory<String, String> consumerFactory;
//
//    @Test
//    void testSendMessage() throws ExecutionException, InterruptedException {
//        // Set up Kafka consumer properties
//        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(group, "true", embeddedKafkaBroker);
//        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//
//        // Create a Kafka consumer
//        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps)
//                .createConsumer();
//
//        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topic);
//
//        // Send a message using the MessageService
//        String testMessage = "Hello, Kafka! " + System.currentTimeMillis();
//
//        SendResult result = messageService.sendMessage(testMessage, topic).get();
//
//        assertThat(result.getProducerRecord().value()).isEqualTo(testMessage);
//
//        // Retrieve and verify the message from Kafka
//        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, topic);
//        assertThat(record).isNotNull();
//        assertThat(record.value()).isEqualTo(testMessage);
//    }
//
//    @Test
//    public void testKafkaIntegration() {
//        // Send a message
//        kafkaTemplate.send(topic, "test-message");
//
//        // Consume the message
//        Consumer<String, String> consumer = consumerFactory.createConsumer();
//        consumer.subscribe(Collections.singleton(topic));
//
//        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
//        assertThat(records.count()).isEqualTo(1);
//        assertThat(records.iterator().next().value()).isEqualTo("test-message");
//    }
//}
//
