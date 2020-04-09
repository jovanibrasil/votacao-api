package com.konoha.votacao.configs;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Sessao;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.modelo.Voto;
import com.konoha.votacao.repository.AssembleiaRepository;
import com.konoha.votacao.repository.ItemPautaRepository;
import com.konoha.votacao.repository.PautaRepository;
import com.konoha.votacao.repository.UsuarioRepository;
import com.konoha.votacao.repository.VotoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

	private final VotoRepository votoRepository;
	private final PautaRepository pautaRepository;
	private final ItemPautaRepository itemPautaRepository;
	private final UsuarioRepository usuarioRepository;
	private final AssembleiaRepository assembleiaRepository;
	
	private Voto voto;
	private Usuario usuario;
	private Pauta pauta;
	private ItemPauta itemPauta;
	private Assembleia assembleia;
	
	@Override
	public void run(String... args) throws Exception {
		
		usuario = new Usuario();
		usuario.setCpf("00000000000");
		usuario.setNomeUsuario("nomeUsuario");
		usuario.setSenha("123456");
		usuario = usuarioRepository.save(usuario);
		
		assembleia = new Assembleia();
		assembleia.setTitulo("Título");
		assembleia = assembleiaRepository.save(assembleia);
		
		Sessao sessao = new Sessao();
		sessao.setDuracaoSessao(3L);
		sessao.setInicioSessao(LocalDateTime.now().minusHours(2L));
		
		
		pauta = new Pauta();
		pauta.setTitulo("Titulo");
		pauta.setDataCriacao(LocalDateTime.now());
		pauta.setAssembleia(assembleia);
		pauta.setSessao(sessao);
		pauta = pautaRepository.save(pauta);
		
		itemPauta = new ItemPauta();
		itemPauta.setTitulo("Titulo");
		itemPauta.setDataCriacao(LocalDateTime.now());
		itemPauta.setPauta(pauta);
		itemPauta = itemPautaRepository.save(itemPauta);
		
		voto = new Voto(usuario, itemPauta, true);
		voto = votoRepository.save(voto);
		
	}

}
