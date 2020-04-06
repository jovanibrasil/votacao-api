package com.konoha.votacao.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PautaDTO extends RepresentationModel<PautaDTO> {

	private Long codPauta;
	private String titulo;
	private String descricao;
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private LocalDateTime dataCriacao;
	private String observacoes;
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private LocalDateTime inicioSessao;
	private Long duracao;
	
}
