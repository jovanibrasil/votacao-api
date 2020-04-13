package com.konoha.votacao.services.impl.modelos;

import lombok.Getter;

@Getter
public class ResultadoItemPauta {

	private Long itemPautaId;
	private Long votosFavoraveis = 0L;
	private Long votosContrarios = 0L;
	
	public ResultadoItemPauta(Long itemPautaId) {
		this.itemPautaId = itemPautaId;
	}
	
	public ResultadoItemPauta(Long itemPautaId, Long votosFavoraveis, Long votosContrarios) {
		this.itemPautaId = itemPautaId;
		this.votosFavoraveis = votosFavoraveis;
		this.votosContrarios = votosContrarios;
	}

	public void addVotoFavoravel() {
		this.votosFavoraveis += 1;
	}
	
	public void addVotoContrario() {
		this.votosContrarios += 1;
	}
	
}
