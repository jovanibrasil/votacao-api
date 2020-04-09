package com.konoha.votacao.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.modelo.Voto;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@DataJpaTest
public class VotoRepositoryTest {

	@Autowired
	private VotoRepository votoRepository;
	
	@Autowired
	private PautaRepository pautaRepository;
	
	@Autowired
	private ItemPautaRepository itemPautaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AssembleiaRepository assembleiaRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Voto voto;
	private Usuario usuario;
	private Pauta pauta;
	private ItemPauta itemPauta;
	private Assembleia assembleia;
	
	@Before
	public void setUp() {
		votoRepository.deleteAll();
		
		usuario = new Usuario();
		usuario.setCpf("00000000000");
		usuario.setNomeUsuario("nomeUsuario");
		usuario.setSenha("123456");
		usuario = usuarioRepository.save(usuario);
		
		assembleia = new Assembleia();
		assembleia.setTitulo("Título");
		assembleia = assembleiaRepository.save(assembleia);
		
		pauta = new Pauta();
		pauta.setTitulo("Titulo");
		pauta.setDataCriacao(LocalDateTime.now());
		pauta.setAssembleia(assembleia);
		pauta = pautaRepository.save(pauta);
		
		itemPauta = new ItemPauta();
		itemPauta.setTitulo("Titulo");
		itemPauta.setDataCriacao(LocalDateTime.now());
		itemPauta.setPauta(pauta);
		itemPauta = itemPautaRepository.save(itemPauta);
		
		voto = new Voto(usuario, itemPauta, true);
		
	}
	
	/**
	 * Salva um voto no banco.
	 * 
	 */
	@Test
	public void testSalvaVoto() {
		votoRepository.save(voto);
		Optional<Voto> optVoto = votoRepository
				.findByVotoIdCodItemPautaAndVotoIdCodUsuario(itemPauta.getCodItemPauta(), usuario.getCodUsuario());
		assertTrue(optVoto.isPresent());
		assertTrue(optVoto.get().getVoto());
		assertEquals(usuario.getCodUsuario(), optVoto.get().getVotoId().getCodUsuario());
		assertEquals(itemPauta.getCodItemPauta(), optVoto.get().getVotoId().getCodItemPauta());
	}
	
	
	@Test(expected = PersistenceException.class)
	public void testSalvaVotoCodItemPautaInvalido() {
		entityManager.detach(itemPauta);
		itemPauta.setCodItemPauta(null);
		voto = new Voto(usuario, itemPauta, true);
		voto = votoRepository.save(voto);
		entityManager.flush();
	}
	
	
	@Test(expected = PersistenceException.class)
	public void testSalvaVotoCodUsuarioInvalido() {
		entityManager.detach(usuario);
		usuario.setCodUsuario(null);
		voto = new Voto(usuario, itemPauta, true);
		votoRepository.save(voto);
		entityManager.flush();
	}
	
	@Test
	public void testBuscaVotoCodUsuarioInvalido() {
		Optional<Voto> optVoto = votoRepository
				.findByVotoIdCodItemPautaAndVotoIdCodUsuario(itemPauta.getCodItemPauta(), -1L);
		assertFalse(optVoto.isPresent());
	}

	@Test//(expected = DataIntegrityViolationException.class)
	public void testSalvaVotosUsuariosDiferentes() {
		votoRepository.save(voto);
		entityManager.detach(usuario);
		usuario.setCodUsuario(null);
		usuario = usuarioRepository.save(usuario);
		
		voto = new Voto(usuario, itemPauta, false);
		votoRepository.save(voto);
		
		assertEquals(2, votoRepository.findAll().size());
	}
	
	@Test
	public void testUpdateVotos() {
		Voto primeiroVoto = votoRepository.save(voto); // voto true
		
		voto = new Voto(usuario, itemPauta, false);
		votoRepository.save(voto); // faz update para false
		
		Voto primeiroVotoSalvo = votoRepository
				.findByVotoIdCodItemPautaAndVotoIdCodUsuario(itemPauta.getCodItemPauta(), usuario.getCodUsuario()).get();
 		
		assertEquals(primeiroVoto.getVoto(), primeiroVotoSalvo.getVoto());
	}
	
	@Test
	public void testBuscaVotoCodItemPautaInvalido() {
		Optional<Voto> optVoto = votoRepository
				.findByVotoIdCodItemPautaAndVotoIdCodUsuario(-1L, usuario.getCodUsuario());
		assertFalse(optVoto.isPresent());
	}
	
}
