package com.konoha.votacao.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.konoha.votacao.modelo.Usuario;

public interface UsuarioService extends UserDetailsService {
	Usuario buscaUsuarioById(Long usuarioId);
}
