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

  private Long duracaoSessao;

}
