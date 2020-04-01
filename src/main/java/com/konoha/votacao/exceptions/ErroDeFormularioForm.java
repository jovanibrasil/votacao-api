package com.konoha.votacao.exceptions;

public class ErroDeFormularioForm {

	private String campo;
	private String erro;
	
	public ErroDeFormularioForm(String campo, String erro) {
		super();
		this.campo = campo;
		this.erro = erro;
	}

	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}
	
}
