package com.konoha.votacao.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.repository.ItemPautaRepository;
import com.konoha.votacao.services.PautaService;

@RunWith(MockitoJUnitRunner.class)
public class ItemPautaServiceTest {

	@Mock
	private ItemPautaRepository itemPautaRepository;
	@Mock
	private PautaService pautaService;
	@InjectMocks
	private ItemPautaServiceImpl itemPautaService;
	
	private final String DESCRICAO = "Uma breve descrição";
	private final String TITULO = "Título";
	private ItemPauta itemPauta;
	
	@Before
	public void setUp() {
        MockitoAnnotations.initMocks(this);
        itemPautaService = new ItemPautaServiceImpl(itemPautaRepository, pautaService);
		itemPauta = new ItemPauta();
		itemPauta.setDescricao(DESCRICAO);
		itemPauta.setTitulo(TITULO);
	}
	
	/**
	 * Testa a persistência de um item de pauta.
	 * 
	 */
	@Test
	public void testSaveItemPauta() {
		when(itemPautaRepository.save(any())).thenAnswer(new Answer<ItemPauta>() {
			@Override
			public ItemPauta answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				ItemPauta itemPauta = (ItemPauta) args[0];
				itemPauta.setCodItemPauta(1L);
				return itemPauta;
			}
		});
		itemPauta = itemPautaService.save(itemPauta);
		assertNotNull(itemPauta);
		assertNotNull(itemPauta.getDataCriacao());
		Long diferencaDeTempo = ChronoUnit.SECONDS
				.between(itemPauta.getDataCriacao(), LocalDateTime.now());
		assertTrue(diferencaDeTempo.doubleValue() >= 0.0);
		assertNotNull(itemPauta.getCodItemPauta());
	}
	
	/**
	 * Testa a busca de um item de pauta existente no banco.
	 * 
	 */
	@Test
	public void testBuscaItemPauta() {
		itemPauta.setCodItemPauta(1L);
		when(itemPautaRepository.findById(1L))
			.thenReturn(Optional.of(itemPauta));
		itemPauta = itemPautaService.findById(1L);
		assertNotNull(itemPauta);
		assertEquals(1L, itemPauta.getCodItemPauta().longValue());
	}
	
	/**
	 * Testa a busca de um item de pauta que não existe no banco.
	 * 
	 */
	@Test(expected = NotFoundException.class)
	public void testBuscaItemPautaNaoExistente() {
		when(itemPautaRepository.findById(2323L))
			.thenReturn(Optional.empty());
		itemPautaService.findById(2323L);
	}
	
	/**
	 * Testa a busca dos itens de uma pauta que existe no banco.
	 * 
	 */
	@Test
	public void testListaItensPautaExistente() {
		PageRequest pageRequest = PageRequest.of(0, 5);
		List<ItemPauta> itens = Arrays.asList(itemPauta, itemPauta);
		// TODO implementar a verificação de pauta no ItemPautaService
		when(itemPautaRepository.findByPautaCodPauta(1L, 
				pageRequest)).thenReturn(new PageImpl<>(itens));
		Page<ItemPauta> page = itemPautaService.findByPautaId(1L, pageRequest);
		assertEquals(2, page.getContent().size());
	}
	
	/**
	 * Testa a busca dos itens de uma pauta que não existe no banco.
	 * 
	 */
	@Test(expected = NotFoundException.class)
	public void testListaItensPautaNaoExistente() {
		when(pautaService.findById(any())).thenThrow(NotFoundException.class);
		itemPautaService.findByPautaId(1L, PageRequest.of(0, 5));
	}
	
}
