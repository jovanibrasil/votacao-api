package com.konoha.votacao.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.konoha.votacao.dto.ItemPautaDTO;
import com.konoha.votacao.forms.ItemPautaForm;
import com.konoha.votacao.mappers.ItemPautaMapper;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.services.ItemPautaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assembleias/{assembleiaId}/pautas/{pautaId}/itens")
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
				.path("/{itemPautaId}")
				.buildAndExpand(itemPauta.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	/**
	 * Item de pauta por ID.
	 * 
	 * @param assembleiaId
	 * @param pautaId
	 * @param itemPautaId
	 * @return
	 */
	@GetMapping("/{itemPautaId}")
	public ResponseEntity<?> buscaItemPauta(@PathVariable Long assembleiaId, @PathVariable Long pautaId, @PathVariable Long itemPautaId){
			
		ItemPauta itemPauta = itemPautaService.findById(itemPautaId);
		ItemPautaDTO itemPautaForm = itemPautaMapper.itemPautaToItemPautaDTO(itemPauta);
		
		return ResponseEntity.ok(itemPautaForm);
	}
	
	/**
	 * Lista itens de uma pauta específica.
	 * 
	 * @param assembleiaId
	 * @param pautaId
	 * @param pageable
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> listaItensPauta(@PathVariable Long assembleiaId, @PathVariable Long pautaId, Pageable pageable){
			
		Page<ItemPauta> itemPautaPage = itemPautaService.findByPautaId(pautaId, pageable);
		Page<ItemPautaDTO> itemPautaDtoPage = itemPautaPage.map(i -> itemPautaMapper.itemPautaToItemPautaDTO(i));		
		return ResponseEntity.ok(itemPautaDtoPage);
	}
	
	
}
