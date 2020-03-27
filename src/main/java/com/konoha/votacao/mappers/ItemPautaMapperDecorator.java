package com.konoha.votacao.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.konoha.votacao.controllers.forms.ItemPautaForm;
import com.konoha.votacao.modelo.ItemPauta;

public abstract class ItemPautaMapperDecorator implements ItemPautaMapper {
	
	private ItemPautaMapper itemPautaMapper;
	
	@Autowired
	public void setItemPautaMapper(ItemPautaMapper itemPautaMapper) {
		this.itemPautaMapper = itemPautaMapper;
	}
	
	@Override
	public ItemPauta itemPautaFormToItemPauta(ItemPautaForm itemPautaForm) {
		ItemPauta itemPauta = itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm);
		// TODO - buscar pauta por id e atribuir ao item de pauta
		return itemPauta;
	}

}
