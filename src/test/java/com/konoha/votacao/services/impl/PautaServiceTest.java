package com.konoha.votacao.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.repository.PautaRepository;

@RunWith(MockitoJUnitRunner.class)
public class PautaServiceTest {

	@Mock
	private PautaRepository pautaRepository;
	@InjectMocks
	private PautaServiceImpl pautaService;
	
	private final String DESCRICAO = "Uma breve descrição";
	private final String TITULO = "Título";
	private Pauta pauta;
	
	@Before
	public void setUp() {
        MockitoAnnotations.initMocks(this);
        pautaService = new PautaServiceImpl(pautaRepository);
        pauta = new Pauta();
        pauta.setDescricao(DESCRICAO);
        pauta.setTitulo(TITULO);
	}
	
	/**
	 * Testa a persistência de uma pauta.
	 * 
	 */
	@Test
	public void testSavePauta() {
		when(pautaRepository.save(any())).thenAnswer(new Answer<Pauta>() {
			@Override
			public Pauta answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Pauta pauta = (Pauta) args[0];
				pauta.setCodPauta(1L);
				return pauta;
			}
		});
		pauta = pautaService.save(pauta);
		assertNotNull(pauta);
		assertNotNull(pauta.getDataCriacao());
		Long diferencaDeTempo = ChronoUnit.SECONDS
				.between(pauta.getDataCriacao(), LocalDateTime.now());
		assertTrue(diferencaDeTempo.doubleValue() >= 0.0);
		assertNotNull(pauta.getCodPauta());
	}
	
}
