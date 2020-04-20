package com.konoha.votacao.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {

	private static final long serialVersionUID = -5787983616724721650L;

	private List<VotoDTO> resultado;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Message - Resultados Votação");
		for (VotoDTO votoDTO : resultado) {
			builder.append(votoDTO.toString() + "\n");
		}
		return builder.toString();
	}
	
}
