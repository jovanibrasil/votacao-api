package com.konoha.votacao.mappers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import com.konoha.votacao.controllers.ItemPautaController;
import com.konoha.votacao.dto.PautaDTO;
import com.konoha.votacao.forms.PautaForm;
import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Sessao;
import com.konoha.votacao.services.AssembleiaService;

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
	
	@Override
	public PautaDTO pautaToPautaDto(Pauta pauta, Long assembleiaId) {
		PautaDTO pautaDto = pautaMapper.pautaToPautaDto(pauta, assembleiaId);
		
		Link link = linkTo(methodOn(ItemPautaController.class).listaItensPauta(assembleiaId, pauta.getId(),
				Pageable.unpaged())).withRel("getItensPauta");
		pautaDto.add(link);
		
		return pautaDto;
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
