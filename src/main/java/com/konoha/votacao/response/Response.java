package com.konoha.votacao.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> { //Definindo o response com um tipo genérico, pois serão vários os tipos de resposta
	
	private T data;
	private T errors;

	public Response(T data) {
		this.data = data;
	}

}
