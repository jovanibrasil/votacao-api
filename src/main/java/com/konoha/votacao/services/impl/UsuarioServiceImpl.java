package com.konoha.votacao.services.impl;

import org.springframework.stereotype.Service;

import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.repository.UsuarioRepository;
import com.konoha.votacao.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private final UsuarioRepository usuarioRepository;
	
	/**
	 * TODO Busca usuário por ID.
	 * 
	 */
	@Override
	public Usuario buscaUsuario(Long usuarioId) {

		Usuario usuario = new Usuario();
		usuario.setCpf("00000000000");
		usuario.setNomeUsuario("nomeUsuario");
		usuario.setSenha("123456");
		usuario = usuarioRepository.save(usuario);
		
		return usuario;
	}	
	
}
