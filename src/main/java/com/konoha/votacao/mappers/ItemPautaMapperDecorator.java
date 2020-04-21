package com.konoha.votacao.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.konoha.votacao.forms.ItemPautaForm;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.services.PautaService;

public abstract class ItemPautaMapperDecorator implements ItemPautaMapper {
	
	private ItemPautaMapper itemPautaMapper;
	private PautaService pautaService;
	
	@Autowired
	public void setItemPautaMapper(ItemPautaMapper itemPautaMapper) {
		this.itemPautaMapper = itemPautaMapper;
	}
	
	@Autowired
	public void setPautaService(PautaService pautaService) {
		this.pautaService = pautaService;
	}
	
	@Override
	public ItemPauta itemPautaFormToItemPauta(ItemPautaForm itemPautaForm) {
		ItemPauta itemPauta = itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm);
		
		Pauta pauta = pautaService.findById(itemPautaForm.getPautaId());
		itemPauta.setPauta(pauta);
		
		return itemPauta;
	}

}
