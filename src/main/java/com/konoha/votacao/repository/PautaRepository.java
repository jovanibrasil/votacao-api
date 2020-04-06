package com.konoha.votacao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.konoha.votacao.modelo.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
	Page<Pauta> findByAssembleiaCodAssembleia(Long codAssembleia, Pageable pageable);
}
