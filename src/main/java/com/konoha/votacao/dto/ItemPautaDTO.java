package com.konoha.votacao.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemPautaDTO {

	private Long id;
	private String titulo;
	private String descricao;
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private LocalDateTime dataCriacao;
	
}
