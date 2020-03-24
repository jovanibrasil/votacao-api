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
@Entity
@NoArgsConstructor
@Table(name = "voto")
public class Voto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Boolean voto;

  @EmbeddedId
  private VotoId votoId;

}
