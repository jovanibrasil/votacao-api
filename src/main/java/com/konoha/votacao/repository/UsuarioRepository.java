package com.konoha.votacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konoha.votacao.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByCpf(String cpf);

}
