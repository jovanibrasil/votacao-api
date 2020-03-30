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

import com.konoha.votacao.controllers.forms.ItemPautaForm;
import com.konoha.votacao.mappers.ItemPautaMapper;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.services.ItemPautaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assembleias/{assembleiaId}/pautas/{pautaId}")
public class ItemPautaController {
	
	private final ItemPautaService itemPautaService;
	private final ItemPautaMapper itemPautaMapper;
	
	/**
	 * Salva um item de pauta.
	 * 
	 * @param itemPautaForm
	 * @param assembleiaId
	 * @param pautaId
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvaItemPauta(@RequestBody @Valid ItemPautaForm itemPautaForm, 
			@PathVariable Long assembleiaId, @PathVariable Long pautaId){
			
		ItemPauta itemPauta = itemPautaService
				.save(itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm));
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/itens/{itemPautaId}")
				.buildAndExpand(itemPauta.getCodItemPauta())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	
}
