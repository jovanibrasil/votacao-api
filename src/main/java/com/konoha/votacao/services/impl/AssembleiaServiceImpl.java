package com.konoha.votacao.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.repository.AssembleiaRepository;
import com.konoha.votacao.services.AssembleiaService;

	
@Service
public class AssembleiaServiceImpl implements AssembleiaService {

	

	@Autowired
	AssembleiaRepository repository;

	@Override
	public Assembleia save(Assembleia assembleia) {
		return repository.save(assembleia);
	}

	@Override
	public Assembleia findById(Long id) {
		Optional<Assembleia> assembleias = repository.findById(id);
		return assembleias.orElse(null);
	}

	@Override
	public Page<Assembleia> findAll(Pageable pageable) {
 
		return repository.findAll(pageable);
	}

}