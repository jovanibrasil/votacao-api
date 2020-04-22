package com.konoha.votacao.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.konoha.votacao.modelo.Assembleia;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@DataJpaTest
public class AssembleiaRepositoryTest {

	@Autowired
	private AssembleiaRepository assembleiaRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	private Assembleia assembleia;
	private final String DESCRICAO = "Descrição";
	private final String TITULO = "Título";

	@Before
	public void setUp() {
		assembleiaRepository.deleteAll();

		assembleia = new Assembleia();
		assembleia.setTitulo(TITULO);
		assembleia.setDescricao(DESCRICAO);
		assembleia.setDataAssembleia(LocalDateTime.of(2020, 03, 30, 20, 0));
		assembleia.setDataCriacao(LocalDateTime.now());
		assembleia.setListaPautas(null);

	}

	/**
	 * Salva uma assembleia válida com todos os dados.
	 * 
	 */
	@Test
	public void testSavePautaValida() {
		assembleia = assembleiaRepository.save(assembleia);
		assertNotNull(assembleia.getId());
	}
	
	/**
	 * Deleta uma assembleia válida com todos os dados.
	 * 
	 */
	@Test
	public void testDeletePautaValida() {
		assembleia = assembleiaRepository.save(assembleia);
		assembleiaRepository.delete(assembleia);
		assertFalse(assembleiaRepository.findById(assembleia.getId()).isPresent());
	}

	/**
	 * Testa busca assembleia por ID
	 */
	@Test
	public void testFindAssembleiaPorId() {
		assembleia = assembleiaRepository.save(assembleia);
		Optional<Assembleia> response = assembleiaRepository.findById(assembleia.getId());
		assertTrue(response.isPresent());
	}

	@Test
	public void testPagina() {
		assembleia = assembleiaRepository.save(assembleia);

		int pageSize = 10;
		Pageable pageable = PageRequest.of(0, pageSize);
		Page<Assembleia> assembleias = assembleiaRepository.findAll(pageable);
		assertEquals(1, assembleias.getContent().size());
	}
	
	/**
   * Testa uma atualizao assembleia
   * 
   */
  @Test
  public void testAtualizarAssembleiaValida() {
    String titulo ="update teste unitário";
    assembleia = assembleiaRepository.save(assembleia);
    assembleia.setTitulo(titulo);
    assembleiaRepository.save(assembleia);
    assertEquals(titulo, assembleia.getTitulo());
    assertThat(assembleia.getTitulo(), is(equalTo(titulo)));

  } 
  
  /**
   * Testa atualização assembleia com título nulo.
   * 
   */
  @Test(expected = DataIntegrityViolationException.class)
  public void testSaveItemPautaSemPauta() {   
    assembleia.setTitulo(null);
    assembleiaRepository.save(assembleia);
    
  }

}
