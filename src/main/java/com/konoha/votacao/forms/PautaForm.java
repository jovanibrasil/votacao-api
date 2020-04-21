package com.konoha.votacao.forms;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PautaForm {
	
	@NotBlank
	@Size(min = 5, max = 50)
	private String titulo;
	@Size(max = 150)
	private String descricao;
	@Size(max = 150)
	private String observacoes;
	private Long assembleiaId;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime inicioSessao;
	private Long duracao;
	
}
