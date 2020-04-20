package com.konoha.votacao.controllers.forms;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AtualizarAssembleiaForm {
	
	
	@Size(max = 50)
	private String titulo;
	@Size(max = 150)
	private String descricao;
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime dataAssembleia;
	
	
}
