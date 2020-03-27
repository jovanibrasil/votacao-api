package com.konoha.votacao.services;

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

import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.repository.ItemPautaRepository;
import com.konoha.votacao.services.impl.ItemPautaServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ItemPautaServiceTest {

	@Mock
	private ItemPautaRepository itemPautaRepository;
	@InjectMocks
	private ItemPautaServiceImpl itemPautaService;
	
	private final String DESCRICAO = "Uma breve descrição";
	private final String TITULO = "Título";
	private ItemPauta itemPauta;
	
	@Before
	public void setUp() {
        MockitoAnnotations.initMocks(this);
        itemPautaService = new ItemPautaServiceImpl(itemPautaRepository);
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
	
}
