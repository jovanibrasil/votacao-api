package com.konoha.votacao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.konoha.votacao.modelo.Assembleia;

@Repository
public interface AssembleiaRepository extends JpaRepository<Assembleia, Long>{
	Page<Assembleia> findAll(Pageable pageable);
}
