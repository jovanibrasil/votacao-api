package com.konoha.votacao.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(name = "cod_usuario", nullable = false)
  private Long id;

  @EqualsAndHashCode.Include
  @Column(name = "cpf", nullable = false)
  private String cpf;

  @Column(name = "nome_usuario", nullable = false)
  private String nomeUsuario;

  @Column(name = "senha", nullable = false)
  private String senha;

}
