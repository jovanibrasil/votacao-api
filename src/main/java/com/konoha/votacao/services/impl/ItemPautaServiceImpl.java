package com.konoha.votacao.services.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.repository.ItemPautaRepository;
import com.konoha.votacao.services.ItemPautaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemPautaServiceImpl implements ItemPautaService {

	private final ItemPautaRepository itemPautaRepository;

	@Override
	public ItemPauta save(ItemPauta itemPauta) {
	
		itemPauta.setDataCriacao(LocalDateTime.now());
		
		return itemPautaRepository.save(itemPauta);
	}

}
