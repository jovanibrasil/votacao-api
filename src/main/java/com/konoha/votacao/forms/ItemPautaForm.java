package com.konoha.votacao.forms;

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
public class ItemPautaForm {

	@NotBlank
	@Size(min = 5, max = 50)
	private String titulo;
	@Size(max = 150)
	private String descricao;
	@NotNull @Positive
	private Long pautaId;
	
}
