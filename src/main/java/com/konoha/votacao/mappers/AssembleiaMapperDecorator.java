package com.konoha.votacao.mappers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import com.konoha.votacao.controllers.PautaController;
import com.konoha.votacao.dto.AssembleiaDTO;
import com.konoha.votacao.modelo.Assembleia;

public abstract class AssembleiaMapperDecorator implements AssembleiaMapper {
	
	private AssembleiaMapper assembleiaMapper;
	
	@Autowired
	public void setAssembleiaMapper(AssembleiaMapper assembleiaMapper) {
		this.assembleiaMapper = assembleiaMapper;
	}
	
	@Override
	public AssembleiaDTO assembleiaToAssembleiaDto(Assembleia assembleia) {
		AssembleiaDTO assembleiaDTO = assembleiaMapper.assembleiaToAssembleiaDto(assembleia);
		Link link = linkTo(methodOn(PautaController.class)
				.listaPautasPorAssembleia(assembleia.getCodAssembleia(), Pageable.unpaged()))
				.withRel("getPautas");
		assembleiaDTO.add(link);
		return assembleiaDTO;
	}

}
