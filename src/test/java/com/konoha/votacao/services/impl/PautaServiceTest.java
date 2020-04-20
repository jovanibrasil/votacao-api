package com.konoha.votacao.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Sessao;
import com.konoha.votacao.repository.PautaRepository;
import com.konoha.votacao.services.ResultadoVotacaoSchedulerService;

@RunWith(MockitoJUnitRunner.class)
public class PautaServiceTest {

	@Mock
	private PautaRepository pautaRepository;
	@Mock
	private ResultadoVotacaoSchedulerService resultadoVotacaoScheduler;
	@InjectMocks
	private PautaServiceImpl pautaService;
	
	private final String DESCRICAO = "Uma breve descrição";
	private final String TITULO = "Título";
	private Pauta pauta;
	
	@Before
	public void setUp() {
        MockitoAnnotations.initMocks(this);
        pautaService = new PautaServiceImpl(pautaRepository, resultadoVotacaoScheduler);
        pauta = new Pauta();
        pauta.setDescricao(DESCRICAO);
        pauta.setTitulo(TITULO);
        Sessao sessao = new Sessao();
        pauta.setSessao(sessao);
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
				pauta.setId(1L);
				return pauta;
			}
		});
		pauta = pautaService.save(pauta);
		assertNotNull(pauta);
		assertNotNull(pauta.getDataCriacao());
		Long diferencaDeTempo = ChronoUnit.SECONDS
				.between(pauta.getDataCriacao(), LocalDateTime.now());
		assertTrue(diferencaDeTempo.doubleValue() >= 0.0);
		assertNotNull(pauta.getId());
	}
	
	/**
	 * Testa a busca por uma pauta existente.
	 */
	@Test
	public void testBuscaPautaPorId() {
		Long pautaId = pauta.getId();
		when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
		pauta = pautaService.findById(pautaId);
		assertNotNull(pauta);
		assertEquals(pautaId, pauta.getId());
	}
	
	/**
	 * Testa a busca por uma pauta que não existe.
	 */
	@Test(expected = NotFoundException.class)
	public void testBuscaPautaPorIdNaoExistente() {
		Long pautaId = 1L;
		when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());
		pautaService.findById(pautaId);
	}
	
	/**
	 * Testa a listagem de pautas de uma assembleia existente.
	 */
	@Test
	public void testBuscaPautasPorAssembleia() {
		Long assembleiaId = 1L;
		PageRequest pageRequest = PageRequest.of(0, 5);
		List<Pauta> pautas = Arrays.asList(pauta, pauta, pauta);
		when(pautaRepository.findByAssembleiaId(assembleiaId, pageRequest))
			.thenReturn(new PageImpl<>(pautas));
		
		Page<Pauta> page = pautaService.findByAssembleiaId(assembleiaId, pageRequest);
		assertEquals(3, page.getContent().size());
	}
	
	/**
	 * Testa a listagem de pautas de uma assembleia que não existe.
	 */
	@Test
	public void testBuscaPautasPorAssembleiaNaoExistente() {
		Long assembleiaId = 1L;
		PageRequest pageRequest = PageRequest.of(0, 5);
		
		when(pautaRepository.findByAssembleiaId(assembleiaId, pageRequest))
			.thenReturn(new PageImpl<>(Arrays.asList()));
		
		Page<Pauta> page = pautaService.findByAssembleiaId(assembleiaId, pageRequest);
		assertEquals(0, page.getContent().size());
	}
	
}
