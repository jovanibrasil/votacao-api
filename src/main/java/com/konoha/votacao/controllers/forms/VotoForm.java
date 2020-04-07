package com.konoha.votacao.controllers.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VotoForm {
	
	@NotNull @Positive
	private Long itemPautaId;
	@NotNull
	private Boolean voto;
	
}
