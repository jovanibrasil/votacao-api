package com.konoha.votacao.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Sessao;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.modelo.Voto;
import com.konoha.votacao.repository.VotoRepository;
import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

@RunWith(MockitoJUnitRunner.class)
public class ComputadorDeVotosTest {
	
	@Mock
	private VotoRepository votoRepository;
	@InjectMocks
	private ComputadorDeVotos computadorDeVotos;
	
	private Voto voto;
	private Usuario usuario;
	private Pauta pauta;
	private ItemPauta itemPauta1;
	private Assembleia assembleia;
	
	@Before
	public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.computadorDeVotos = new ComputadorDeVotos(votoRepository);
        
        usuario = new Usuario();
		usuario.setCodUsuario(1L);
		usuario.setCpf("00000000000");
		usuario.setNomeUsuario("nomeUsuario");
		usuario.setSenha("123456");
		
		assembleia = new Assembleia();
		assembleia.setTitulo("Título");
		assembleia.setCodAssembleia(1L);
		
		pauta = new Pauta();
		pauta.setCodPauta(1L);
		pauta.setTitulo("Titulo");
		pauta.setDataCriacao(LocalDateTime.now());
		pauta.setAssembleia(assembleia);
		Sessao sessao = new Sessao();
		sessao.setInicioSessao(LocalDateTime.now().minusHours(5L));
		sessao.setDuracaoSessao(4L);
		pauta.setSessao(sessao);
		
		itemPauta1 = new ItemPauta();
		itemPauta1.setCodItemPauta(1L);
		itemPauta1.setTitulo("Titulo");
		itemPauta1.setDataCriacao(LocalDateTime.now());
		itemPauta1.setPauta(pauta);
		
		voto = new Voto(usuario, itemPauta1, true);
        
	}

	/**
	 * Testa a computação de votos de uma pauta, testando uma pauta com um item de
	 * pauta, que recebeu um voto favorável.
	 */
	@Test
	public void testComputaVotosPauta() {
		List<Voto> votos = Arrays.asList(voto);
		pauta.setListaItemPautas(Arrays.asList(itemPauta1));
		
		when(votoRepository.findByVotoIdCodItemPauta(any())).thenReturn(votos);
		
		List<ResultadoItemPauta> resultado = computadorDeVotos.computaVotos(pauta);
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
		
		List<Voto> votos = Arrays.asList(voto, new Voto(new Usuario(), itemPauta1, false));
		
		pauta.setListaItemPautas(Arrays.asList(itemPauta1));
		when(votoRepository.findByVotoIdCodItemPauta(any())).thenReturn(votos);
		
		List<ResultadoItemPauta> resultado = computadorDeVotos.computaVotos(pauta);
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
		
		ItemPauta itemPauta2 = new ItemPauta();
		itemPauta2.setCodItemPauta(2L);
		itemPauta2.setPauta(pauta);
		voto = new Voto(usuario, itemPauta2, true);
		
		pauta.setListaItemPautas(Arrays.asList(itemPauta1, itemPauta2));
		
		when(votoRepository.findByVotoIdCodItemPauta(itemPauta1.getCodItemPauta()))
			.thenReturn(Arrays.asList(voto));
		
		when(votoRepository.findByVotoIdCodItemPauta(itemPauta2.getCodItemPauta()))
			.thenReturn(Arrays.asList(voto));
		
		List<ResultadoItemPauta> resultado = computadorDeVotos.computaVotos(pauta);
		
		assertEquals(2, resultado.size());
		assertEquals(1, resultado.get(0).getVotosFavoraveis());
		assertEquals(1, resultado.get(1).getVotosFavoraveis());
	}

}
