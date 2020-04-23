package com.konoha.votacao.services.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.repository.UsuarioRepository;
import com.konoha.votacao.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private final UsuarioRepository usuarioRepository;
	
	/**
	 * Busca usuário por ID.
	 * 
	 */
	@Override
	public Usuario buscaUsuarioById(Long usuarioId) {
		Optional<Usuario> optUsuario = usuarioRepository.findById(usuarioId);
		
		if(optUsuario.isPresent()) {
			return optUsuario.get();
		}
		
		throw new NotFoundException("Usuário não encontrado");
	}

	/**
	 * Busca usuário por nome.
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<Usuario> optUsuario = usuarioRepository.findByCpf(username);
		
		if(optUsuario.isPresent()) {
			return optUsuario.get();
		}
		
		throw new UsernameNotFoundException("Usuário não encontrado");
	}

	/**
	 * Busca usuário por CPF
	 * 
	 */
	@Override
	public Usuario findByCpf(String cpf) {
		Optional<Usuario> optUsuario = usuarioRepository.findByCpf(cpf);
		
		if(optUsuario.isPresent()) {
			return optUsuario.get();
		}
		
		throw new NotFoundException("Usuário não encontrado");
	}	
	
}
