package com.konoha.votacao.services.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.repository.PautaRepository;
import com.konoha.votacao.services.PautaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

	private final PautaRepository pautaRepository;

	@Override
	public Pauta save(Pauta pauta) {
		
		pauta.setDataCriacao(LocalDateTime.now());
		
		return pautaRepository.save(pauta);
	}
	
}
