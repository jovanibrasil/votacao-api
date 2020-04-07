package com.konoha.votacao.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.konoha.votacao.modelo.Assembleia;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AssembleiaRepositoryTest {

	@Autowired
	AssembleiaRepository assembleiaRepository;

	private Assembleia assembleia;
	private final String DESCRICAO = "blabla";
	private final String TITULO = "Nnanana";

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
		assertNotNull(assembleia.getCodAssembleia());
	}

	/**
	 * Testa busca assembleia por ID
	 */
	@Test
	public void testFindAssembleiaPorId() {
		assembleia = assembleiaRepository.save(assembleia);
		Optional<Assembleia> response = assembleiaRepository.findById(assembleia.getCodAssembleia());
		assertTrue(response.isPresent());
	}

	@Test
	public void testPagina() {
		assembleia = assembleiaRepository.save(assembleia);

		int pageSize = 10;
		Pageable pageable = PageRequest.of(0, pageSize);
		Page<Assembleia> assembleias = assembleiaRepository.findAll(pageable);
		assertEquals(4, assembleias.getContent().size());
	}

}
