package com.konoha.votacao.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.konoha.votacao.modelo.ItemPauta;

public interface ItemPautaService {
	ItemPauta save(ItemPauta itemPauta);
	ItemPauta findById(Long id);
	Page<ItemPauta> findByPautaId(Long id, Pageable pageable);
}
