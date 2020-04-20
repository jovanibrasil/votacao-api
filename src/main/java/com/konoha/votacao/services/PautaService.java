package com.konoha.votacao.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.konoha.votacao.modelo.Pauta;

public interface PautaService {
	
	Pauta save(Pauta pauta);
	Pauta findById(Long pautaId);
	Page<Pauta> findByAssembleiaId(Long assembleiaId, Pageable pageable);
	void deleteById(Long id);
	
}
