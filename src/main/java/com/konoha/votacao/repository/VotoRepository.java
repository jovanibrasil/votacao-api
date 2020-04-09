package com.konoha.votacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konoha.votacao.modelo.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long> {

	Optional<Voto> findByVotoIdCodItemPautaAndVotoIdCodUsuario(Long codItemPauta, Long codUsuario);
	List<Voto> findByVotoIdCodItemPauta(Long codItemPauta);
	
}
