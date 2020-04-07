package com.konoha.votacao.services.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.konoha.votacao.exceptions.VotoException;
import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Sessao;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.modelo.Voto;
import com.konoha.votacao.repository.VotoRepository;
import com.konoha.votacao.services.ItemPautaService;
import com.konoha.votacao.services.UsuarioService;

@RunWith(MockitoJUnitRunner.class)
public class VotoServiceTest {

	@Mock
	private ItemPautaService itemPautaService;
	@Mock
	private VotoRepository votoRepository;
	@Mock
	private UsuarioService usuarioService;
	@InjectMocks
	private VotoServiceImpl votoService;
	
	private Voto voto;
	private Usuario usuario;
	private Pauta pauta;
	private ItemPauta itemPauta;
	private Assembleia assembleia;
	
	@Before
	public void setUp() {
        MockitoAnnotations.initMocks(this);
        votoService = new VotoServiceImpl(itemPautaService, votoRepository, usuarioService);
		
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
		sessao.setInicioSessao(LocalDateTime.now().minusHours(1L));
		sessao.setDuracaoSessao(5L);
		pauta.setSessao(sessao);
		
		itemPauta = new ItemPauta();
		itemPauta.setCodItemPauta(1L);
		itemPauta.setTitulo("Titulo");
		itemPauta.setDataCriacao(LocalDateTime.now());
		itemPauta.setPauta(pauta);
		
		voto = new Voto(usuario, itemPauta, true);
		
	}
	
	/**
	 * Testa registro de um voto em um item de uma pauta aberta.
	 */
	@Test
	public void testSalvaVoto() {
		
		when(usuarioService.buscaUsuario(any())).thenReturn(usuario);
		when(itemPautaService.findById(any())).thenReturn(itemPauta);
		
		when(votoRepository.findByVotoIdCodItemPautaAndVotoIdCodUsuario(1L,
				usuario.getCodUsuario())).thenReturn(Optional.empty());
		
		when(votoRepository.save(any())).thenReturn(voto);
		
		votoService.salvarVoto(1L, true);
		
	}
	
	/**
	 * Testa registro de um voto repetido. Só pode haver um voto por pauta
	 * para cada usuário.
	 */
	@Test(expected = VotoException.class)
	public void testSalvaVotoRepetido() {
		
		when(usuarioService.buscaUsuario(any())).thenReturn(usuario);
		when(votoRepository.findByVotoIdCodItemPautaAndVotoIdCodUsuario(1L,
				usuario.getCodUsuario())).thenReturn(Optional.of(voto));
		votoService.salvarVoto(1L, true);
		
	}
	
	/**
	 * Testa registro de um voto em um item de uma pauta fechada.
	 */
	@Test(expected = VotoException.class)
	public void testSalvaVotoPautaInvalida() {
		pauta.getSessao().setDuracaoSessao(0L);
		
		when(usuarioService.buscaUsuario(any())).thenReturn(usuario);
		when(itemPautaService.findById(any())).thenReturn(itemPauta);
		
		when(votoRepository.findByVotoIdCodItemPautaAndVotoIdCodUsuario(1L,
				usuario.getCodUsuario())).thenReturn(Optional.empty());
		
		votoService.salvarVoto(1L, true);
		
	}
	
	
}
