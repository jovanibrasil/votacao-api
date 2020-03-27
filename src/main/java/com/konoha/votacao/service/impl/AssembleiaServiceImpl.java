package com.konoha.votacao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.repository.AssembleiaRepository;
import com.konoha.votacao.service.AssembleiaService;

import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
@Service
public class AssembleiaServiceImpl implements AssembleiaService{

//	private final AssembleiaRepository repository;
	
	@Autowired
	AssembleiaRepository repository;
	
	@Override
	public Assembleia save(Assembleia assembleia) {
		return repository.save(assembleia);
	}

}
