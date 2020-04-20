package com.konoha.votacao.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "item_pauta")
public class ItemPauta implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cod_item_pauta")
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "titulo", nullable = false)
  private String titulo;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "data_criacao")
  private LocalDateTime dataCriacao;
  
  @ManyToOne
  @JoinColumn(name = "cod_pauta", nullable = false)
  private Pauta pauta;

}
