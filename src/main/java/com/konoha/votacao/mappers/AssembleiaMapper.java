package com.konoha.votacao.mappers;

import org.mapstruct.Mapper;

import com.konoha.votacao.dto.AssembleiaDTO;
import com.konoha.votacao.modelo.Assembleia;

@Mapper
public interface AssembleiaMapper {

	AssembleiaDTO assembleiaToAssembleiaDto(Assembleia assembleia);

	Assembleia assembleiaDtoToAssembleia(AssembleiaDTO assembleiaDto);

}
