package com.konoha.votacao.configs.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.konoha.votacao.dto.MessageDTO;


@Configuration
@EnableKafka // ebnable kafka listner
public class KafkaConsumerConfig {

	@Value("${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	/**
	 * { link @ConsumerFactory} possui as estratégias para configuração de um
	 * {link @Consumer}.
	 * 
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, MessageDTO> consumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "json");

		return new DefaultKafkaConsumerFactory<>(
				configProps, 
				new StringDeserializer(),
				new JsonDeserializer<>(MessageDTO.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, MessageDTO> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MessageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

}
