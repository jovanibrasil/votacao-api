package com.konoha.votacao.controllers;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.konoha.votacao.dto.PautaDTO;
import com.konoha.votacao.forms.PautaForm;
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
	public ResponseEntity<?> salvaPauta(@RequestBody @Valid PautaForm pautaForm, @PathVariable Long assembleiaId) {

		pautaForm.setAssembleiaId(assembleiaId);
		Pauta pauta = pautaService.save(pautaMapper.pautaFormToPauta(pautaForm));

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{pautaId}")
				.buildAndExpand(pauta.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	/**
	 * Busca uma pauta por ID.
	 * 
	 * @param pautaId
	 * @param assembleiaId
	 * @return
	 */
	@GetMapping("/{pautaId}")
	public ResponseEntity<PautaDTO> buscaPauta(@PathVariable Long pautaId, @PathVariable Long assembleiaId) {

		Pauta pauta = pautaService.findById(pautaId);
		PautaDTO pautaDto = pautaMapper.pautaToPautaDto(pauta, assembleiaId);
		return ResponseEntity.ok(pautaDto);
	}

	/**
	 * Busca uma uma lista de pautas de uma assembleia.
	 * 
	 * @param pautaId
	 * @param assembleiaId
	 * @return
	 */
	@GetMapping
	public ResponseEntity<Page<PautaDTO>> listaPautasPorAssembleia(@PathVariable Long assembleiaId, Pageable pageable) {

		Page<Pauta> pautas = pautaService.findByAssembleiaId(assembleiaId, pageable);
		Page<PautaDTO> pautaDtoList = pautas.map(pauta -> pautaMapper.pautaToPautaDto(pauta, assembleiaId));
		return ResponseEntity.ok(pautaDtoList);
	}
	
	/**
	 * Remove uma pauta pelo ID, iformado.
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {		
		pautaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
