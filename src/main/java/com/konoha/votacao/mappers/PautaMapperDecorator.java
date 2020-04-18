package com.konoha.votacao.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.konoha.votacao.controllers.forms.PautaForm;
import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Sessao;
import com.konoha.votacao.services.AssembleiaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PautaMapperDecorator implements PautaMapper {

	private PautaMapper pautaMapper;
	private AssembleiaService assembleiaService;
	
	@Override
	public Pauta pautaFormToPauta(PautaForm pautaForm) {
		Pauta pauta = pautaMapper.pautaFormToPauta(pautaForm);
		
		Sessao sessao = new Sessao();
		sessao.setInicioSessao(pautaForm.getInicioSessao());
		sessao.setDuracaoSessao(pautaForm.getDuracao());
		pauta.setSessao(sessao);
		Assembleia assembleia = assembleiaService.findById(pautaForm.getAssembleiaId());
		pauta.setAssembleia(assembleia);
		return pauta;
	}

	@Autowired
	public void setPautaMapper(PautaMapper pautaMapper) {
		this.pautaMapper = pautaMapper;
	}

	@Autowired
	public void setAssembleiaService(AssembleiaService assembleiaService) {
		this.assembleiaService = assembleiaService;
	}
	
}
