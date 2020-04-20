package com.konoha.votacao.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.exceptions.VotoException;
import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Sessao;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.modelo.Voto;
import com.konoha.votacao.repository.VotoRepository;
import com.konoha.votacao.services.ItemPautaService;
import com.konoha.votacao.services.PautaService;
import com.konoha.votacao.services.UsuarioService;
import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

@RunWith(MockitoJUnitRunner.class)
public class VotoServiceTest {

	@Mock
	private ItemPautaService itemPautaService;
	@Mock
	private VotoRepository votoRepository;
	@Mock
	private UsuarioService usuarioService;
	@Mock
	private PautaService pautaService;
	@Mock
	private ComputadorDeVotos computadorDeVotos;
	@InjectMocks
	private VotoServiceImpl votoService;
	
	private Voto voto;
	private Usuario usuario;
	private Pauta pauta;
	private ItemPauta itemPauta1;
	private Assembleia assembleia;
	
	@Before
	public void setUp() {
        MockitoAnnotations.initMocks(this);
        votoService = new VotoServiceImpl(itemPautaService, votoRepository,
        		usuarioService, pautaService, computadorDeVotos);
		
		usuario = new Usuario();
		usuario.setId(1L);
		usuario.setCpf("00000000000");
		usuario.setNomeUsuario("nomeUsuario");
		usuario.setSenha("123456");
		
		assembleia = new Assembleia();
		assembleia.setTitulo("Título");
		assembleia.setId(1L);
		
		pauta = new Pauta();
		pauta.setId(1L);
		pauta.setTitulo("Titulo");
		pauta.setDataCriacao(LocalDateTime.now());
		pauta.setAssembleia(assembleia);
		Sessao sessao = new Sessao();
		sessao.setInicioSessao(LocalDateTime.now().minusHours(5L));
		sessao.setDuracaoSessao(4L);
		pauta.setSessao(sessao);
		
		itemPauta1 = new ItemPauta();
		itemPauta1.setId(1L);
		itemPauta1.setTitulo("Titulo");
		itemPauta1.setDataCriacao(LocalDateTime.now());
		itemPauta1.setPauta(pauta);
		
		voto = new Voto(usuario, itemPauta1, true);
		
	}
	
	/**
	 * Testa registro de um voto em um item de uma pauta aberta.
	 */
	@Test
	public void testSalvaVoto() {
		
		// configura a sessão como aberta
		Sessao sessao = new Sessao();
		sessao.setInicioSessao(LocalDateTime.now());
		sessao.setDuracaoSessao(4L);
		pauta.setSessao(sessao);
		
		when(usuarioService.buscaUsuario(any())).thenReturn(usuario);
		when(itemPautaService.findById(any())).thenReturn(itemPauta1);
		
		when(votoRepository.findByVotoIdItemPautaIdAndVotoIdUsuarioId(1L,
				usuario.getId())).thenReturn(Optional.empty());
		
		when(votoRepository.save(any())).thenReturn(voto);
		
		votoService.saveVoto(1L, true);
		
	}
	
	/**
	 * Testa registro de um voto repetido. Só pode haver um voto por pauta
	 * para cada usuário.
	 */
	@Test(expected = VotoException.class)
	public void testSalvaVotoRepetido() {
		
		when(usuarioService.buscaUsuario(any())).thenReturn(usuario);
		when(votoRepository.findByVotoIdItemPautaIdAndVotoIdUsuarioId(1L,
				usuario.getId())).thenReturn(Optional.of(voto));
		votoService.saveVoto(1L, true);
		
	}
	
	/**
	 * Testa registro de um voto em um item de uma pauta fechada.
	 */
	@Test(expected = VotoException.class)
	public void testSalvaVotoPautaInvalida() {
		pauta.getSessao().setDuracaoSessao(0L);
		
		when(usuarioService.buscaUsuario(any())).thenReturn(usuario);
		when(itemPautaService.findById(any())).thenReturn(itemPauta1);
		
		when(votoRepository.findByVotoIdItemPautaIdAndVotoIdUsuarioId(1L,
				usuario.getId())).thenReturn(Optional.empty());
		
		votoService.saveVoto(1L, true);
		
	}
	
	/**
	 * Testa a computação de votos de uma pauta, testando uma pauta com um item de
	 * pauta, que recebeu um voto favorável.
	 */
	@Test
	public void testComputaVotosPauta() {
		List<ResultadoItemPauta> resultados = Arrays.asList(new ResultadoItemPauta(1L, 1L, 0L));
		
		when(pautaService.findById(pauta.getId())).thenReturn(pauta);
		when(computadorDeVotos.computaVotos(any())).thenReturn(resultados);
		
		List<ResultadoItemPauta> resultado = votoService.findResultadoVotacaoByPautaId(pauta.getId());
		ResultadoItemPauta rip = resultado.get(0);
		
		assertEquals(1, resultado.size());
		assertEquals(1, rip.getVotosFavoraveis());
		assertEquals(0, rip.getVotosContrarios());
	}
	
	/**
	 * Testa a computação de votos de uma pauta com um item de pauta
	 * que recebeu um voto favorável e um contrário.
	 * 
	 */
	@Test
	public void testComputaVotosPautaEmpate() {
		
		List<ResultadoItemPauta> resultados = Arrays.asList(new ResultadoItemPauta(1L, 1L, 1L));
		
		when(pautaService.findById(pauta.getId())).thenReturn(pauta);
		when(computadorDeVotos.computaVotos(any())).thenReturn(resultados);
		
		List<ResultadoItemPauta> resultado = votoService.findResultadoVotacaoByPautaId(pauta.getId());
		ResultadoItemPauta rip = resultado.get(0);
		
		assertEquals(1, resultado.size());
		assertEquals(1, rip.getVotosFavoraveis());
		assertEquals(1, rip.getVotosContrarios());
	}
	
	/**
	 * Testa a computação de votos de uma pauta com dois itens pauta cada um 
	 * com um voto vaforável.
	 * 
	 */
	@Test
	public void testComputaVotosPautaComDoisTestes() {
		
		List<ResultadoItemPauta> resultados = Arrays.asList(
				new ResultadoItemPauta(1L, 1L, 0L),
				new ResultadoItemPauta(1L, 1L, 0L));
		
		when(pautaService.findById(pauta.getId())).thenReturn(pauta);
		when(computadorDeVotos.computaVotos(any())).thenReturn(resultados);
		
		List<ResultadoItemPauta> resultado = votoService
				.findResultadoVotacaoByPautaId(pauta.getId());
		
		assertEquals(2, resultado.size());
		assertEquals(1, resultado.get(0).getVotosFavoraveis());
		assertEquals(1, resultado.get(1).getVotosFavoraveis());
	}
	
	/**
	 * Testa a computação de votos de uma pauta não existente.
	 * 
	 */
	@Test(expected = NotFoundException.class)
	public void testComputaVotosPautaNaoExistente() {
		when(pautaService.findById(pauta.getId())).thenThrow(NotFoundException.class);
		votoService.findResultadoVotacaoByPautaId(pauta.getId());
	}
	
	/**
	 * Testar a computação de votos em uma pauta com sessão não concuída.
	 * 
	 */
	@Test(expected = VotoException.class)
	public void testComputaVotosPautaAberta() {
		
		// Configura a sessão como aberta
		Sessao sessao = new Sessao();
		sessao.setInicioSessao(LocalDateTime.now());
		sessao.setDuracaoSessao(4L);
		pauta.setSessao(sessao);
		
		when(pautaService.findById(pauta.getId())).thenReturn(pauta);
		votoService.findResultadoVotacaoByPautaId(pauta.getId());
	}
	
}
