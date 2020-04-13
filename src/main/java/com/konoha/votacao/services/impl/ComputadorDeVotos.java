package com.konoha.votacao.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Voto;
import com.konoha.votacao.repository.VotoRepository;
import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ComputadorDeVotos {

	private final VotoRepository votoRepository;
	
	/**
	 * Computa os votos dos itens de uma pauta. O retorno é uma lista de objetos do 
	 * tipo @ResultadoItemPauta.
	 * 
	 * @param pauta
	 * @return
	 */
	public List<ResultadoItemPauta> computaVotos(Pauta pauta){
		return pauta.getListaItemPautas().stream().map(itemPauta -> {
			// Conta quantos votos favoráveis e quantos contrários um item de pauta possui.
			ResultadoItemPauta result = new ResultadoItemPauta(itemPauta.getCodItemPauta());
			List<Voto> votos = votoRepository.findByVotoIdCodItemPauta(itemPauta.getCodItemPauta());
			
			votos.stream().forEach(voto -> {
				if(Boolean.TRUE.equals(voto.getVoto())) {
					result.addVotoFavoravel();
				}else {
					result.addVotoContrario();
				}
			});			
			return result;
		}).collect(Collectors.toList());
	}
	
}
