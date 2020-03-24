package com.konoha.votacao.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "assembleia")
public class Assembleia implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cod_assembleia")
  @EqualsAndHashCode.Include
  private Long codAssembleia;

  @NotNull
  @Column(name = "titulo")
  private String titulo;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "data_assembleia")
  private LocalDateTime dataAssembleia;

  @EqualsAndHashCode.Include
  @Column(name = "data_criacao")
  private LocalDateTime dataCriacao;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "assembleia")
  private List<Pauta> listaPautas = new ArrayList<>();

}
