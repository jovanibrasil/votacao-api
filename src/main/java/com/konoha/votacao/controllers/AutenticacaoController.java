package com.konoha.votacao.controllers;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konoha.votacao.configs.security.TokenService;
import com.konoha.votacao.dto.TokenDTO;
import com.konoha.votacao.forms.LoginForm;
import com.konoha.votacao.modelo.Usuario;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

	private final AuthenticationManager authManager;
	private final TokenService tokenService;
	
	/**
	 * Faz a autenticação de um usuário. A autenticação éfeita com JWT.
	 * 
	 * @param loginForm
	 * @return
	 */
	@PostMapping
	public ResponseEntity<TokenDTO> autenticar(@Valid @RequestBody LoginForm loginForm){
		
		try {
			UsernamePasswordAuthenticationToken dadosLogin = new UsernamePasswordAuthenticationToken(loginForm.getCpf(), loginForm.getSenha());
			Authentication auth = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken((Usuario)auth.getPrincipal());
			return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
