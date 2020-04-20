package com.konoha.votacao.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "voto")
public class Voto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Boolean voto;

  @EmbeddedId
  private VotoId votoId;
  
  public Voto(Usuario usuario, ItemPauta itemPauta, Boolean voto) {
	  this.votoId = new VotoId(itemPauta.getId(), 
			  usuario.getId());
	  this.voto = voto;
  }

}
