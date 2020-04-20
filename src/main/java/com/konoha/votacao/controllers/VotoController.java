package com.konoha.votacao.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konoha.votacao.controllers.forms.VotoForm;
import com.konoha.votacao.dto.VotoDTO;
import com.konoha.votacao.mappers.VotoMapper;
import com.konoha.votacao.services.VotoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votos")
public class VotoController {

	private final VotoService votoService;
	private final VotoMapper votoMapper;
	
	/**
	 * Salva um voto no sistema. 
	 * 
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvarVoto(@RequestBody @Valid VotoForm votoForm){
		votoService.saveVoto(votoForm.getItemPautaId(), votoForm.getVoto());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}
	
	/**
	 * Retorna o resultado de votação dos itens de uma pauta. O retorno será feito com sucesso 
	 * somente se a pauta existir e estar fechada.
	 * 
	 * @param pautaId
	 * @return
	 */
	@GetMapping("/{pautaId}")
	public ResponseEntity<List<VotoDTO>> buscaVotosDeUmaPauta(@PathVariable Long pautaId){
		
		List<VotoDTO> resultados = votoService.findResultadoVotacaoByPautaId(pautaId).stream()
				.map(votoMapper::resultadoItemPautaToVotoDto)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(resultados);
	}
	
}
