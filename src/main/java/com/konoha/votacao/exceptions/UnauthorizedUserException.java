package com.konoha.votacao.exceptions;

public class UnauthorizedUserException extends RuntimeException {

	private static final long serialVersionUID = -7208724883860788211L;

	public UnauthorizedUserException(String msg) {
		super(msg);
	}
	
}
