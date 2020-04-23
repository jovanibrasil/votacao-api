package com.konoha.votacao.configs.data;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Perfil;
import com.konoha.votacao.modelo.Sessao;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.modelo.Voto;
import com.konoha.votacao.repository.AssembleiaRepository;
import com.konoha.votacao.repository.ItemPautaRepository;
import com.konoha.votacao.repository.PautaRepository;
import com.konoha.votacao.repository.PerfilRepository;
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
	private final PerfilRepository perfilRepository;
	
	private Voto voto;
	private Usuario admin;
	private Pauta pauta;
	private ItemPauta itemPauta;
	private Assembleia assembleia;
	
	@Override
	public void run(String... args) throws Exception {
		
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		
		Perfil perfilUser = new Perfil();
		perfilUser.setName("ROLE_USER");
		perfilUser = perfilRepository.save(perfilUser);
		Perfil perfilAdmin = new Perfil();
		perfilAdmin.setName("ROLE_ADMIN");
		perfilAdmin = perfilRepository.save(perfilAdmin);
		
		Usuario usuario = new Usuario();
		usuario.setCpf("10879065044");
		usuario.setNomeUsuario("usuario");
		usuario.setSenha(bCrypt.encode("123456"));
		usuario.setPerfis(Arrays.asList(perfilUser));
		usuarioRepository.save(usuario);
		
		admin = new Usuario();
		admin.setCpf("05589097010");
		admin.setNomeUsuario("admin");
		admin.setSenha(bCrypt.encode("123456"));
		admin.setPerfis(Arrays.asList(perfilAdmin));
		admin = usuarioRepository.save(admin);
		
		assembleia = new Assembleia();
		assembleia.setTitulo("Assembleia 0");
		assembleia.setDescricao("Descrição Assembleia 0");
		assembleia = assembleiaRepository.save(assembleia);
		
		/**
		 * Pauta fechada
		 */
		Sessao sessao = new Sessao();
		sessao.setDuracaoSessao(1L);
		sessao.setInicioSessao(LocalDateTime.now().minusHours(2L));
		
		pauta = new Pauta();
		pauta.setTitulo("Pauta 0");
		pauta.setDataCriacao(LocalDateTime.now());
		pauta.setAssembleia(assembleia);
		pauta.setSessao(sessao);
		pauta = pautaRepository.save(pauta);
		
		itemPauta = new ItemPauta();
		itemPauta.setTitulo("Item de Pauta 0");
		itemPauta.setDataCriacao(LocalDateTime.now());
		itemPauta.setPauta(pauta);
		itemPauta = itemPautaRepository.save(itemPauta);
		
		voto = new Voto(admin, itemPauta, true);
		voto = votoRepository.save(voto);
		
		assembleia = new Assembleia();
		assembleia.setTitulo("Assembleia 1");
		assembleia.setDescricao("Descrição Assembleia 1");
		assembleia = assembleiaRepository.save(assembleia);
		
		sessao = new Sessao();
		sessao.setDuracaoSessao(3L);
		sessao.setInicioSessao(LocalDateTime.now().minusHours(2L));
		
		
		pauta = new Pauta();
		pauta.setTitulo("Pauta 0 Assb 1");
		pauta.setDataCriacao(LocalDateTime.now());
		pauta.setAssembleia(assembleia);
		pauta.setSessao(sessao);
		pauta = pautaRepository.save(pauta);
		
		itemPauta = new ItemPauta();
		itemPauta.setTitulo("Item de Pauta 0 Assb 1");
		itemPauta.setDataCriacao(LocalDateTime.now());
		itemPauta.setPauta(pauta);
		itemPauta = itemPautaRepository.save(itemPauta);
		
		voto = new Voto(admin, itemPauta, true);
		voto = votoRepository.save(voto);
		
	}

}