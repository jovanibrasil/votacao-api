package com.konoha.votacao.mappers;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.konoha.votacao.controllers.forms.PautaForm;
import com.konoha.votacao.modelo.Pauta;

@Mapper
@DecoratedWith(PautaMapperDecorator.class)
public interface PautaMapper {
	
	Pauta pautaFormToPauta(PautaForm pautaForm);

}
