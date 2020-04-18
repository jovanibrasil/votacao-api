package com.konoha.votacao.modelo;

import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Sessao {

	private LocalDateTime inicioSessao;

	private Long duracaoSessao; // tempo em horas

	/**
	 * Faz a verificação se a sessão está aberta. A duração da sessão é dada em
	 * horas e a contagem é feita a partir de um horário de inicio. Uma sessão está
	 * aberta se o hora atual é anterior ao horário de termino da sessão.
	 * 
	 * @return
	 */
	public boolean isAberta() {
		if (inicioSessao == null || duracaoSessao == null) {
			return false;
		}
		LocalDateTime termino = inicioSessao.plusHours(duracaoSessao);
		return LocalDateTime.now().isBefore(termino);
	}

	public LocalDateTime getDataFechamento() {
		return inicioSessao.plusHours(duracaoSessao);
	}

}
