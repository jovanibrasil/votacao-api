package com.konoha.votacao.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor @AllArgsConstructor
@Embeddable
public class VotoId implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Column(name="cod_item_pauta")
  private ItemPauta codItemPauta;
  
  @Column(name="cod_usuario")
  private Usuario codUsuario;

  

}
