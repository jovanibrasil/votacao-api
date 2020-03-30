package com.konoha.votacao.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

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
	@NotNull @Positive
	private Long assembleiaId;
	
}
