package com.konoha.votacao.services;

import com.konoha.votacao.dto.MessageDTO;

public interface AsyncMessageService {
	void sendMessage(MessageDTO message);
	void receive(MessageDTO message);
}
