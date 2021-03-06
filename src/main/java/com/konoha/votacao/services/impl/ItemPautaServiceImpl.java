package com.konoha.votacao.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.repository.ItemPautaRepository;
import com.konoha.votacao.services.ItemPautaService;
import com.konoha.votacao.services.PautaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemPautaServiceImpl implements ItemPautaService {

	private final ItemPautaRepository itemPautaRepository;
	private final PautaService pautaService;
	
	@Override
	public ItemPauta save(ItemPauta itemPauta) {
	
		itemPauta.setDataCriacao(LocalDateTime.now());
		
		return itemPautaRepository.save(itemPauta);
	}

	@Override
	public Page<ItemPauta> findByPautaId(Long pautaId, Pageable pageable) {
		pautaService.findById(pautaId); // Lança exception caso não exista a pauta
		return itemPautaRepository.findByPautaId(pautaId, pageable);
	}

	@Override
	public ItemPauta findById(Long id) {
		Optional<ItemPauta> itemPauta = itemPautaRepository.findById(id);
		
		if(itemPauta.isPresent()) {
			return itemPauta.get();
		}
		
		throw new NotFoundException("Item de pauta não encontrado");
	}

}
