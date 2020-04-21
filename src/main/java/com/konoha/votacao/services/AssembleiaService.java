package com.konoha.votacao.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.konoha.votacao.modelo.Assembleia;

public interface AssembleiaService {
	
	Assembleia save(Assembleia assembleia);
	Assembleia findById(Long id);
	Page<Assembleia> findAll(Pageable pageable);
	void deleteById(Long id);
  Assembleia atualizar(Assembleia assembleia);	
	
}
