package com.konoha.votacao.services;

import java.util.List;

import com.konoha.votacao.modelo.Voto;
import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

public interface VotoService {
	void salvarVoto(Long itemPautaId, Boolean voto);
	List<ResultadoItemPauta> computaVotosPauta(Long pautaId);
	List<Voto> buscaVotosByItemPautaId(Long itemPautaId);
}
