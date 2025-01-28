package com.example.kafkademo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;

@SpringBootApplication
public class KafkademoApplication {
	@Value("${application.topic}")
	private String topic;

	private static final Logger logger = LoggerFactory.getLogger(KafkademoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkademoApplication.class, args);
	}

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(topic)
				.partitions(2)
				.replicas(1)
				.build();
	}

	@KafkaListener(groupId = "${application.group}", topics = "${application.topic}")
	public void listen(String in) {
		logger.info("message: {}", in);
	}


}
