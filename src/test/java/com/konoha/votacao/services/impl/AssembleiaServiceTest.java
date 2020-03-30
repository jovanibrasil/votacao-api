package com.konoha.votacao.services.impl;

import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.repository.AssembleiaRepository;
import com.konoha.votacao.services.AssembleiaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AssembleiaServiceTest {
	
	private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
	
	@MockBean
	AssembleiaRepository assembleiaRepository;
	
	@Autowired
	AssembleiaService assembleiaService;
	
	@Test
	public void testDataCriacao() {
		
		Assembleia assembleia = new Assembleia();
		assembleia.setDataCriacao(DATA_CRIACAO);
		
		when(assembleiaRepository.save(Mockito.any(Assembleia.class))).thenReturn(assembleia);
		
		assembleia = assembleiaService.save(assembleia);
		assertNotNull(assembleia);
	}
	
}