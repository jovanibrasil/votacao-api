package com.konoha.votacao.repository;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.repository.AssembleiaRepository;

import lombok.RequiredArgsConstructor;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
//@RequiredArgsConstructor
public class AssembleiaRepositoryTest {
	
//	private final AssembleiaRepository repository;
	
	@Autowired
	AssembleiaRepository assembleiaRepository;
	
	@Test
	public void testSave() {
		
		Assembleia assembleia = new Assembleia();
		assembleia.setTitulo("Votação aumento salarial");
		assembleia.setDescricao("Aumento salarial dos bancários");
		assembleia.setDataAssembleia(LocalDateTime.of(2020, 03, 30, 20, 0));
		assembleia.setDataCriacao(LocalDateTime.now());
		assembleia.setListaPautas(null);
		
		Assembleia response = assembleiaRepository.save(assembleia);
		
		assertNotNull(response);
	}
}
