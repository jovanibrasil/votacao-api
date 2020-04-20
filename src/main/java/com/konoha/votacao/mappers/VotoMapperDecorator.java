package com.konoha.votacao.mappers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import com.konoha.votacao.controllers.ItemPautaController;
import com.konoha.votacao.dto.VotoDTO;
import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.repository.ItemPautaRepository;
import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

public abstract class VotoMapperDecorator implements VotoMapper {

	private VotoMapper votoMapper;
	private ItemPautaRepository itemPautaRepository;
	
	@Transactional
	@Override
	public VotoDTO resultadoItemPautaToVotoDto(ResultadoItemPauta resultadoItemPauta) {
		VotoDTO votoDto = votoMapper.resultadoItemPautaToVotoDto(resultadoItemPauta);
		
		Optional<ItemPauta> optItemPauta = itemPautaRepository.findById(resultadoItemPauta.getItemPautaId());
		
		if(!optItemPauta.isPresent()) {
			throw new NotFoundException("Não existe assembleia com o item de pauta que se quer votar.");
		}
		
		Long assembleiaId = optItemPauta.get().getPauta().getAssembleia().getId();
		
		Link link = linkTo(methodOn(ItemPautaController.class)
				.listaItensPauta(assembleiaId, resultadoItemPauta.getItemPautaId(), Pageable.unpaged()))
				.withRel("getItemPauta");
		
		votoDto.add(link);
		return votoDto;
	}
	
	@Autowired
	public void setVotoMapper(VotoMapper votoMapper) {
		this.votoMapper = votoMapper;
	}
	
	@Autowired
	public void setItemPautaService(ItemPautaRepository itemPautaService) {
		this.itemPautaRepository = itemPautaService;
	}
	
}
