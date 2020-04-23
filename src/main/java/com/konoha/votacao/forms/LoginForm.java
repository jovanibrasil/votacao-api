package com.konoha.votacao.forms;

import javax.validation.constraints.NotBlank;

import com.konoha.votacao.validators.Cpf;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginForm {
	@Cpf 
	private String cpf;
	@NotBlank
	private String senha;
}
