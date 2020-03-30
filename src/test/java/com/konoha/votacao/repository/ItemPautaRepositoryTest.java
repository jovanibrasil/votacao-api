package com.konoha.votacao.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ItemPautaRepositoryTest {

	@Autowired
	private ItemPautaRepository itemPautaRepository;
	@Autowired
	private PautaRepository pautaRepository;
	@Autowired
	private AssembleiaRepository assembleiaRepository;
	
	private ItemPauta itemPauta;
	private final String DESCRICAO = "Uma breve descrição";
	private final String TITULO = "Título";

	@Before
	public void setUp() {		
		itemPautaRepository.deleteAll();
		
		Assembleia assembleia = new Assembleia();
		assembleia.setTitulo("Título");
		assembleiaRepository.save(assembleia);
		itemPauta = new ItemPauta();
		itemPauta.setDescricao(DESCRICAO);
		itemPauta.setTitulo(TITULO);
		Pauta pauta = new Pauta();
		pauta.setTitulo("Título da pauta");
		pauta.setAssembleia(assembleia);
		pauta.setDataCriacao(LocalDateTime.now());
		pauta = pautaRepository.save(pauta);
		itemPauta.setPauta(pauta);
	}

	/**
	 * Salva um item de pauta e verifica se os valores salvos corresponsem
	 * com os definidos no objeto. Também verifica se o ID foi atribuído ao
	 * registro pelo banco.
	 * 
	 */
	@Test
	public void testSaveItemPautaValida() {
		itemPauta = itemPautaRepository.save(itemPauta);
		assertEquals(DESCRICAO, itemPauta.getDescricao());
		assertEquals(TITULO, itemPauta.getTitulo());
		assertNotNull(itemPauta.getCodItemPauta());
	}
	
	/**
	 * Tenta salvar um item de pauta com título nulo.
	 * 
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveItemPautaSemTitulo() {
		itemPauta.setTitulo(null);
		itemPauta = itemPautaRepository.save(itemPauta);
	}
	
	/**
	 * Tenta salvar um item de pauta com título nulo.
	 * 
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveItemPautaSemPauta() {
		itemPauta.setPauta(null);
		itemPauta = itemPautaRepository.save(itemPauta);
	}

	/**
	 * Persiste 3 itens de pauta no banco. Verifica
	 * se os 3 itens de pauta estão persistidos.
	 * 
	 */
	@Test
	public void testSaveItensPautaValida() {
		itemPautaRepository.save(itemPauta);
		itemPauta.setCodItemPauta(null);
		itemPautaRepository.save(itemPauta);
		itemPauta.setCodItemPauta(null);
		itemPautaRepository.save(itemPauta);
		assertEquals(3, itemPautaRepository.findAll().size());
	}
	
	/**
	 * Testa o update da propriedade descrição. Todos as demais
	 * propriedades devem permanecer como antes.
	 * 
	 */
	@Test
	public void testUpdateDescricaoItemPautaValida() {
		String novaDescricao = "Nova Descrição";
		
		itemPauta = itemPautaRepository.save(itemPauta); // save
		itemPauta.setDescricao(novaDescricao);
		Long id = itemPauta.getCodItemPauta();
		itemPauta = itemPautaRepository.save(itemPauta); // update
		
		assertEquals(novaDescricao, itemPauta.getDescricao());
		assertEquals(TITULO, itemPauta.getTitulo());
		assertEquals(id, itemPauta.getCodItemPauta());
	}

	/**
	 * Testa o update da propriedade título. Todos as demais
	 * propriedades devem permanecer como antes.
	 * 
	 */
	@Test
	public void testUpdateTituloItemPautaValida() {

		String novoTitulo = "Novo Título";

		itemPauta = itemPautaRepository.save(itemPauta); // save
		itemPauta.setTitulo(novoTitulo); 
		Long id = itemPauta.getCodItemPauta();
		itemPauta = itemPautaRepository.save(itemPauta); // update

		assertEquals(novoTitulo, itemPauta.getTitulo());
		assertEquals(DESCRICAO, itemPauta.getDescricao());
		assertEquals(id, itemPauta.getCodItemPauta());
	}

	/**
	 * Testa a busca por ID de um item de pauta salvo na base de dados.
	 * 
	 */
	@Test
	public void testFindItemPautaPorId() {
		itemPauta = itemPautaRepository.save(itemPauta);
		Optional<ItemPauta> optSavedItem = itemPautaRepository.findById(itemPauta.getCodItemPauta());
		assertTrue(optSavedItem.isPresent());
	}
	
	/**
	 * Testa a busca por ID de um item de pauta não salvo na base de dados.
	 * 
	 */
	@Test
	public void testFindItemNaoExistentePautaPorId() {
		Optional<ItemPauta> optSavedItem = itemPautaRepository.findById(0L);
		assertFalse(optSavedItem.isPresent());
	}
	
	/**
	 * Testa a exclusão de um item por ID. O item a ser excluído está persistido no banco de dados.
	 * 
	 */
	@Test
	public void testDeleteItemExistentePautaPorId() {
		itemPauta = itemPautaRepository.save(itemPauta);
		itemPautaRepository.deleteById(itemPauta.getCodItemPauta());
		assertFalse(itemPautaRepository.findById(itemPauta.getCodItemPauta()).isPresent());
		assertEquals(0, itemPautaRepository.findAll().size());
	}
		
	
}
