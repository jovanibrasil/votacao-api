package com.konoha.votacao.mappers;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.konoha.votacao.dto.AssembleiaDTO;
import com.konoha.votacao.forms.AtualizarAssembleiaForm;
import com.konoha.votacao.modelo.Assembleia;

@Mapper
@DecoratedWith(AssembleiaMapperDecorator.class)
public interface AssembleiaMapper {

	/**
	 * Converte um objeto Assembleia para um objeto AssembleiaDto.
	 * 
	 * @param assembleia
	 * @return
	 */
	AssembleiaDTO assembleiaToAssembleiaDto(Assembleia assembleia);

	/**
	 * Converte um objeto AssembleiaDto para um objeto Assembleia. 
	 * 
	 * @param assembleiaDto
	 * @return
	 */
	Assembleia assembleiaDtoToAssembleia(AssembleiaDTO assembleiaDto);
	
	/**
	 * Converte um objeto AtualizaAssembleiaForm para um objeto Assembleia.
	 * @param atualizarAssembleiaForm
	 * @return
	 */
	Assembleia atualizarAssembleiaFormToAssembleia(AtualizarAssembleiaForm atualizarAssembleiaForm);

}
