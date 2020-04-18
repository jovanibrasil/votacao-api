package com.konoha.votacao.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.konoha.votacao.exceptions.VotoException;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.modelo.Voto;
import com.konoha.votacao.repository.VotoRepository;
import com.konoha.votacao.services.ItemPautaService;
import com.konoha.votacao.services.PautaService;
import com.konoha.votacao.services.UsuarioService;
import com.konoha.votacao.services.VotoService;
import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

	private final ItemPautaService itemPautaService;
	private final VotoRepository votoRepository;
	private final UsuarioService usuarioService;
	private final PautaService pautaService;
	private final ComputadorDeVotos computadorDeVotos;
	
	/**
	 * Registra um voto no sistema. Um voto é dado por um usuário devidamente registrado no sistema para um item de pauta 
	 * pertencente a uma pauta com sessão aberta. O usuário é buscado por meio do  ID contido no JWT, esse ID é obtido quando 
	 * a requisição chega e é interceptada, então o token é extraído do cabeçalho Authorization. 
	 * 
	 */
	@Transactional
	@Override
	public void saveVoto(Long itemPautaId, Boolean votoValue) {
		// TODO ID do usuário deve ser buscado do contexto de segurança
		Long usuarioId = 1L;
		Usuario usuario = usuarioService.buscaUsuario(usuarioId);
		
		// Verifica se o voto já foi cadastrado no sistema
		Optional<Voto> optVotoSalvo = votoRepository.findByVotoIdCodItemPautaAndVotoIdCodUsuario(itemPautaId, usuario.getCodUsuario());
		if(optVotoSalvo.isPresent()) {
			throw new VotoException("Voto já registrado no sistema.");
		}
		
		// Verifica se o item de pauta pertence a uma pauta aberta
		ItemPauta itemPauta = itemPautaService.findById(itemPautaId);
		
		if(itemPauta.getPauta().isAberta()) {
			// TODO Verifica se o usuário pode votar na pauta
			Voto voto = new Voto(usuario, itemPauta, votoValue);
			votoRepository.save(voto);
		}else {
			throw new VotoException("Pauta não está aberta");
		}
		
	}
	
	/**
	 * Retorna uma lista de resultados de votação para uma pauta que
	 * está com sessão aberta. 
	 * 
	 * @param pautaId é o id da pauta que se quer os resultados
	 * 
	 */
	@Transactional
	@Override
	public List<ResultadoItemPauta> findResultadoVotacaoByPautaId(Long pautaId) {
		Pauta pauta = pautaService.findById(pautaId);
		if(pauta.isAberta()) throw new VotoException("Votação não está fechada ainda. Teste mais tarde.");
		return computadorDeVotos.computaVotos(pauta);
	}
	
}
