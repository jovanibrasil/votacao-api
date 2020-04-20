package com.konoha.votacao.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Getter
@Setter
@NoArgsConstructor
public class AssembleiaDTO {
	
	private Long codAssembleia;
	@NotBlank
	@Length(min = 5, max = 50, message = "O título deve ter no máximo 50 caracteres")
	private String titulo;
	@NotBlank
	@Length(min = 5, max = 100, message = "A descrição deve ter no máximo 100 caracteres")
	private String descricao;	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss")
	private LocalDateTime dataAssembleia;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss")	
	private LocalDateTime dataCriacao;
	
}
