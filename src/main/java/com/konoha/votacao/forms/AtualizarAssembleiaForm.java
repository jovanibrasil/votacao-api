package com.konoha.votacao.forms;

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
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime dataAssembleia;
	
	
}
