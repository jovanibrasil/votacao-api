package com.konoha.votacao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.konoha.votacao.modelo.ItemPauta;

public interface ItemPautaRepository extends JpaRepository<ItemPauta, Long> {
	Page<ItemPauta> findByPautaId(Long id, Pageable pageable);
}
