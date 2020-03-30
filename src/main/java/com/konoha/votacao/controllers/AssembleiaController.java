package com.konoha.votacao.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konoha.votacao.dto.AssembleiaDTO;
import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.response.Response;
import com.konoha.votacao.services.AssembleiaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("assembleia")
@RequiredArgsConstructor
public class AssembleiaController {
	
	private final AssembleiaService assembleiaService;
	
	@PostMapping
	public ResponseEntity<Response<AssembleiaDTO>> create(@Valid @RequestBody AssembleiaDTO dto, BindingResult result) {
		
		Response<AssembleiaDTO> response = new Response<AssembleiaDTO>();
		
		Assembleia assembleia = assembleiaService.save(this.convertDtoToEntity(dto));
		
		response.setData(this.convertEntityToDto(assembleia));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	private Assembleia convertDtoToEntity(AssembleiaDTO dto) {
		Assembleia assembleia = new Assembleia();
		
		assembleia.setTitulo(dto.getTitulo());
		assembleia.setDescricao(dto.getDescricao());
		assembleia.setDataAssembleia(dto.getDataAssembleia());
		assembleia.setDataCriacao(dto.getDataCriacao());
		assembleia.setListaPautas(dto.getListaPautas());
		
		return assembleia;
	}
	
	private AssembleiaDTO convertEntityToDto(Assembleia assembleia) {
		
		AssembleiaDTO dto = new AssembleiaDTO();
		
		dto.setTitulo(assembleia.getTitulo());
		dto.setDescricao(assembleia.getDescricao());
		dto.setDataAssembleia(assembleia.getDataAssembleia());
		dto.setDataCriacao(assembleia.getDataCriacao());
		dto.setListaPautas(assembleia.getListaPautas());
		
		return dto;
	}
	
}
