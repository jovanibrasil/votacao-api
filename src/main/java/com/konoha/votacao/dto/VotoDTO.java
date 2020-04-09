package com.konoha.votacao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VotoDTO {

	private Long itemPautaId;
	private Long votosFavoraveis;
	private Long votosContrarios;
	
}
