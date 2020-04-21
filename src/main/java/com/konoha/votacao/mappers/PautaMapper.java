package com.konoha.votacao.mappers;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.konoha.votacao.dto.PautaDTO;
import com.konoha.votacao.forms.PautaForm;
import com.konoha.votacao.modelo.Pauta;

@Mapper
@DecoratedWith(PautaMapperDecorator.class)
public interface PautaMapper {
	
	/**
	 * Faz a conversão de um objeto PautaForm para um objeto Pauta.
	 * 
	 * @param pautaForm
	 * @return
	 */
	Pauta pautaFormToPauta(PautaForm pautaForm);
	
	/**
	 * Faz a conversão de um objeto Pauta para um objeto PautaDto.
	 * 
	 * @param pauta
	 * @return
	 */
	PautaDTO pautaToPautaDto(Pauta pauta, Long assembleiaId);

}
