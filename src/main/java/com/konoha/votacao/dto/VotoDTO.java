package com.konoha.votacao.dto;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VotoDTO extends RepresentationModel<VotoDTO> implements Serializable {

	private static final long serialVersionUID = 7417274190788466523L;

	private Long votosFavoraveis;
	private Long votosContrarios;
	
	@Override
	public String toString() {
		return "VotoDTO [ votosFavoraveis=" + votosFavoraveis + ", votosContrarios="
				+ votosContrarios + " links=" + super.getLinks().toString()  + "]";
	}
	
}
