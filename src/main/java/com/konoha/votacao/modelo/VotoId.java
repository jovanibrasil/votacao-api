package com.konoha.votacao.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor 
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class VotoId implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Column(name="cod_item_pauta", nullable = false)
  private Long itemPautaId;
  
  @Column(name="cod_usuario", nullable = false)
  private Long usuarioId;

}
