package com.konoha.votacao.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.konoha.votacao.controllers.forms.PautaForm;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.services.AssembleiaService;
import com.konoha.votacao.services.ItemPautaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PautaMapperDecorator implements PautaMapper {

	private PautaMapper pautaMapper;
	private final AssembleiaService assembleiaService;
	private final ItemPautaService itemPautaService;
	
	@Autowired
	public void setPautaMapper(PautaMapper pautaMapper) {
		this.pautaMapper = pautaMapper;
	}
	
	@Override
	public Pauta pautaFormToPauta(PautaForm pautaForm) {
		Pauta pauta = pautaMapper.pautaFormToPauta(pautaForm);
		
		// TODO buscar no banco e configura itens de pauta e assembleia
		
		return pauta;
	}

}
