package com.konoha.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konoha.votacao.modelo.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, Long> {}
