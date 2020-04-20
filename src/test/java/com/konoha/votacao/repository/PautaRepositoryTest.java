package com.konoha.votacao.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Sessao;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@DataJpaTest
public class PautaRepositoryTest {

	@Autowired
	private PautaRepository pautaRepository;
	@Autowired
	private AssembleiaRepository assembleiaRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	private final String TITULO = "Título";
	private final String DESCRICAO = "Descricao";
	private final LocalDateTime DATA_CRIACAO_PAUTA = LocalDateTime.of(2020, Month.JANUARY, 05, 14, 10);
	private final String OBSERVACOES = "Observações";
	private final List<ItemPauta> LISTA_ITENS_PAUTA = new ArrayList<>();
	private final LocalDateTime INICIO_SESSAO = LocalDateTime.of(2020, Month.MARCH, 01, 10, 00);
	private final Long DURACAO_SESSAO = 600L;
	
	private Pauta pauta;
	private Assembleia assembleia;
	private Sessao sessao;
	
	@Before
	public void setUp() {
		pautaRepository.deleteAll();
		assembleiaRepository.deleteAll();
		
		sessao = new Sessao();
		sessao.setInicioSessao(INICIO_SESSAO);
		sessao.setDuracaoSessao(DURACAO_SESSAO);
		
		assembleia = new Assembleia();
		assembleia.setTitulo(TITULO);
		assembleiaRepository.save(assembleia);
		
		pauta = new Pauta();
		pauta.setTitulo(TITULO);
		pauta.setDescricao(DESCRICAO);
		pauta.setDataCriacao(DATA_CRIACAO_PAUTA);
		pauta.setObservacoes(OBSERVACOES);
		pauta.setListaItemPautas(LISTA_ITENS_PAUTA);
		pauta.setSessao(sessao);
		pauta.setAssembleia(assembleia);
		
	}
	
	/**
	 * Salva uma pauta válida com todos os dados.
	 * 
	 */
	@Test
	public void testSavePautaValida() {
		pauta = pautaRepository.save(pauta);
		assertNotNull(pauta.getId());
	}
	
	/**
	 * Salva uma pauta com título null, ou seja, a pauta é inválida.
	 * 
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void testSavePautaInvalidaTituloNull() {
		pauta.setTitulo(null);
		pauta = pautaRepository.save(pauta);
	}
	
	/**
	 * Salva uma pauta com título sem referência para uma assembléia, a pauta é inválida.
	 * 
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void testSavePautaInvalidaAssembleiaNull() {
		pauta.setAssembleia(null);
		pauta = pautaRepository.save(pauta);
	}
	
	/**
	 * Busca por Id uma pauta salva no banco.
	 * 
	 */
	@Test
	public void testBuscaPautaPorId() {
		pautaRepository.save(pauta);
		entityManager.detach(pauta);
		pauta.setId(null);
		pauta = pautaRepository.save(pauta);
		assertTrue(pautaRepository.findById(pauta.getId()).isPresent());
	}
	
	/**
	 * Lista pelas pautas registradas no banco. Primeiro são inseridas quatro
	 * pautas no banco e então elas são listadas.
	 */
	@Test
	public void testListaPautas() {
		pautaRepository.save(pauta);
		entityManager.detach(pauta);
		pauta.setId(null);
		pautaRepository.save(pauta);
		entityManager.detach(pauta);
		pauta.setId(null);
		pautaRepository.save(pauta);
		entityManager.detach(pauta);
		pauta.setId(null);
		pautaRepository.save(pauta);
		assertEquals(4, pautaRepository.findAll().size());
	}
	
	/**
	 * Lista pelas pautas registradas no banco relacionadas a uma
	 * assembleia específica.
	 */
	@Test
	public void testListaPautasByAssembleiaId() {
		pautaRepository.save(pauta);
		entityManager.detach(pauta);
		pauta.setId(null);
		pautaRepository.save(pauta);
		entityManager.detach(pauta);
		pauta.setId(null);
		pautaRepository.save(pauta);
		entityManager.detach(pauta);
		pauta.setId(null);
		pautaRepository.save(pauta);
		PageRequest pageRequest = PageRequest.of(0, 5);
		assertEquals(4, pautaRepository.findByAssembleiaId(assembleia.getId(), pageRequest).getContent().size());
	}
	
	/**
	 * Lista pelas pautas registradas no banco relacionadas a uma
	 * assembleia que não existe.
	 */
	@Test
	public void testListaPautasByAssembleiaIdNaoExistente() {
		PageRequest pageRequest = PageRequest.of(0, 5);
		assertEquals(0, pautaRepository.findByAssembleiaId(-1L, pageRequest).getContent().size());
	}
	
	/**
	 * Testa a exclusão de uma pauta registrada no banco de dados.
	 * 
	 */
	@Test
	public void testDeletePautaPorId() {
		pauta = pautaRepository.save(pauta);
		pautaRepository.deleteById(pauta.getId());
		assertFalse(pautaRepository.findById(pauta.getId()).isPresent());
	}
	
	/**
	 * Testa o update de uma pauta. A alteração dos dados de sessão é
	 * alterada em outro teste.
	 * 
	 */
	@Test
	public void testUpdatePauta() {
		String novoTitulo = "Novo Título";
		String novaDescricao = "Nova Descricao";
		String novasObservacoes = "Novas Observações";
		
		pauta = pautaRepository.save(pauta);
		pauta.setTitulo(novoTitulo);
		pauta.setDescricao(novaDescricao);
		pauta.setObservacoes(novasObservacoes);
		
		pauta = pautaRepository.save(pauta);
		assertEquals(novoTitulo, pauta.getTitulo());
		assertEquals(novaDescricao, pauta.getDescricao());
		assertEquals(novasObservacoes, pauta.getObservacoes());
	}
	
	/**
	 * Testa o update de uma sessão, que é um objeto aninhado dentro
	 * da pauta.
	 * 
	 */
	@Test
	public void testUpdateSessao() {
		LocalDateTime novoInicioSessao = LocalDateTime.of(2020, Month.APRIL, 02, 14, 00);
		Long novaDuracaoSessao = 600L;
		pauta = pautaRepository.save(pauta);
		Sessao sessao = pauta.getSessao();
		sessao.setDuracaoSessao(novaDuracaoSessao);
		sessao.setInicioSessao(novoInicioSessao);
		pauta = pautaRepository.save(pauta);
		sessao = pauta.getSessao();
		assertEquals(novaDuracaoSessao, sessao.getDuracaoSessao());
		assertEquals(novoInicioSessao, sessao.getInicioSessao());
	}
	
	
	
}
