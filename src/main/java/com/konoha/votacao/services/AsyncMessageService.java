package com.konoha.votacao.services;

import com.konoha.votacao.dto.Message;

public interface AsyncMessageService {
	void sendMessage(Message message);
	void receive(Message message);
}
