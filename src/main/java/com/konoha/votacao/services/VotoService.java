package com.konoha.votacao.services;

import java.util.List;

import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

public interface VotoService {
	void saveVoto(Long itemPautaId, Boolean voto);
	List<ResultadoItemPauta> findResultadoVotacaoByPautaId(Long pautaId);
}
