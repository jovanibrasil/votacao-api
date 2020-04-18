package com.konoha.votacao.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VotoDTO implements Serializable {

	private static final long serialVersionUID = 7417274190788466523L;

	private Long itemPautaId;
	private Long votosFavoraveis;
	private Long votosContrarios;
	
	@Override
	public String toString() {
		return "VotoDTO [itemPautaId=" + itemPautaId + ", votosFavoraveis=" + votosFavoraveis + ", votosContrarios="
				+ votosContrarios + "]";
	}
	
}
