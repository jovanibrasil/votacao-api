package com.konoha.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konoha.votacao.modelo.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {}
