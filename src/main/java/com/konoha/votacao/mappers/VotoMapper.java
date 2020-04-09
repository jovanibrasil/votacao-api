package com.konoha.votacao.mappers;

import org.mapstruct.Mapper;

import com.konoha.votacao.dto.VotoDTO;
import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

@Mapper
public interface VotoMapper {

	VotoDTO resultadoItemPautaToVotoDto(ResultadoItemPauta resultadoItemPauta);
	
}
