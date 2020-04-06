package com.konoha.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konoha.votacao.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}
