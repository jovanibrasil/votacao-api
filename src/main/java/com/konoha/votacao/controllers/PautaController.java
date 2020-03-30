package com.konoha.votacao.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.konoha.votacao.controllers.forms.PautaForm;
import com.konoha.votacao.mappers.PautaMapper;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.services.PautaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assembleias/{assembleiaId}/pautas")
public class PautaController {

	private final PautaService pautaService;
	private final PautaMapper pautaMapper;
	
	/**
	 * Salva uma pauta.
	 * 
	 * @param pautaForm
	 * @param assembleiaId
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvaPauta(@RequestBody @Valid PautaForm pautaForm, 
			@PathVariable Long assembleiaId){
		
		Pauta pauta = pautaService
				.save(pautaMapper.pautaFormToPauta(pautaForm));
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{pautaId}")
				.buildAndExpand(pauta.getCodPauta())
				.toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
}
