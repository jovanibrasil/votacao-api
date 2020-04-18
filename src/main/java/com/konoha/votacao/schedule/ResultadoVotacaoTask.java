package com.konoha.votacao.schedule;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.konoha.votacao.dto.Message;
import com.konoha.votacao.dto.VotoDTO;
import com.konoha.votacao.mappers.VotoMapper;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.services.AsyncMessageService;
import com.konoha.votacao.services.VotoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ResultadoVotacaoTask implements Runnable {

	private final UUID taskId;
	private final Pauta pauta;
	private final AsyncMessageService asyncMessageService;
	private final VotoService votoService;
	private final VotoMapper votoMapper;
	
	public UUID getId() {
		return taskId;
	}

	@Override
	public void run() {
		log.info("Sending message ...");
		List<VotoDTO> votos = votoService.findResultadoVotacaoByPautaId(pauta.getCodPauta())
			.stream().map(votoMapper::resultadoItemPautaToVotoDto).collect(Collectors.toList());	
		Message message = new Message(votos);
		asyncMessageService.sendMessage(message);
	}	
	
}
