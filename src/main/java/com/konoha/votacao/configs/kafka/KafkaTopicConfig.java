package com.konoha.votacao.configs.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

	@Value(value="${kafka.bootstrapAddress}")
	private String bootstrapAddress;
	
	/**
	 * Configuração do kafka admin responsável por adicionar os novos tópicos
	 * 
	 * @return
	 */
	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}
	
	/**
	 * Cria um tópico programaticamente
	 * 
	 * @return
	 */
	@Bean
	public NewTopic topic() {
		return new NewTopic("compasso-uol-poa", 1, (short) 1);
	}
	
}
