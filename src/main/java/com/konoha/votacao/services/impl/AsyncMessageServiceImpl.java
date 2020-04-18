package com.konoha.votacao.services.impl;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.konoha.votacao.dto.Message;
import com.konoha.votacao.services.AsyncMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AsyncMessageServiceImpl implements AsyncMessageService {

	private final KafkaTemplate<String, Message> kafkaTemplate;
	
	public AsyncMessageServiceImpl(KafkaTemplate<String, Message> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	/**
	 * Envia uma mensagem com o resultado da votação de uma pauta e lida com o resultado de forma assíncrona.
	 * 
	 * @param message
	 */
	@Override
	public void sendMessage(Message message) {
		ListenableFuture<SendResult<String, Message>> send = kafkaTemplate
				.send("compasso-uol-poa", message);
		send.addCallback(new ListenableFutureCallback<SendResult<String, Message>>() {
			@Override
			public void onSuccess(SendResult<String, Message> result) {
				log.info("Mensagem enviada. Offset = {}", result.getRecordMetadata().offset());
			}
			@Override
			public void onFailure(Throwable ex) {
				log.info("Erro ao enviar a mensagem. {}", ex.getMessage());
			}
		});
	}
	
	/**
	 * 
	 * 
	 * @param message
	 */
	@KafkaListener(topics = "compasso-uol-poa")
	@Override
	public void receive(Message message) {
		log.info("Mensagem recebida {}", message.toString());
	}
	
}
