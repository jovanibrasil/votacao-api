package com.konoha.votacao.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konoha.votacao.controllers.forms.VotoForm;
import com.konoha.votacao.services.VotoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votos")
public class VotoController {

	public final VotoService votoService;
	
	/**
	 * Salva um voto no sistema. 
	 * 
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvarVoto(@RequestBody @Valid VotoForm votoForm){
		votoService.salvarVoto(votoForm.getItemPautaId(), votoForm.getVoto());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}
	
}
