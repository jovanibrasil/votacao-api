package com.konoha.votacao.exceptions;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8559171583014622625L;

	public NotFoundException(String message) {
		super(message);
	}
	
}
