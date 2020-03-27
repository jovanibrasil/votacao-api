package com.konoha.votacao.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.konoha.votacao.modelo.Pauta;

import lombok.Data;

@Data
public class AssembleiaDTO {
	
	private Long codAssembleia;
	@NotNull
	@Length(min = 5, max = 50, message = "O título deve ter no máximo 50 caracteres")
	private String titulo;
	@NotBlank
	@Length(min = 5, max = 100, message = "A descrição deve ter no máximo 100 caracteres")
	private String descricao;
	private LocalDateTime dataAssembleia;
	private LocalDateTime dataCriacao;
	private List<Pauta> listaPautas;
}
