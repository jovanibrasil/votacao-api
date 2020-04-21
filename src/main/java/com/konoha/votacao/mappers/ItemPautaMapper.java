package com.konoha.votacao.mappers;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.konoha.votacao.dto.ItemPautaDTO;
import com.konoha.votacao.forms.ItemPautaForm;
import com.konoha.votacao.modelo.ItemPauta;

@Mapper
@DecoratedWith(ItemPautaMapperDecorator.class)
public interface ItemPautaMapper {
	
	ItemPauta itemPautaFormToItemPauta(ItemPautaForm itemPautaForm);
	ItemPautaDTO itemPautaToItemPautaDTO(ItemPauta itemPauta);

}
