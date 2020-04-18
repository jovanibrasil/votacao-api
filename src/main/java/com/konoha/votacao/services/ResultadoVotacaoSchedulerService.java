package com.konoha.votacao.services;

import java.util.UUID;

import com.konoha.votacao.modelo.Pauta;

public interface ResultadoVotacaoSchedulerService {
	UUID scheduleTask(Pauta pauta);
	void removeTask(UUID taskUUID);
}
