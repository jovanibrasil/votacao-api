package com.konoha.votacao.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.konoha.votacao.dto.AssembleiaDTO;
import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.service.AssembleiaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AssembleiaControllerTest {
	
	private static final Long COD_ASSEMBLEIA = 1L;
	private static final String TITULO = "Aumento de investimento";
	private static final String DESCRICAO = "Aumento do investimento agrário";
	private static final LocalDateTime DATA_ASSEMBLEIA = LocalDateTime.of(2020, 03, 30, 20, 0);
	private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
	private static final List<Pauta> LISTA_PAUTAS = null;
	private static final String URL = "/assembleia";
	
	@MockBean
	AssembleiaService assembleiaService;
	
	@Autowired
	MockMvc mvc;
	
	@Test
	public void testSave() throws Exception {
		
		BDDMockito.given(assembleiaService.save(Mockito.any(Assembleia.class))).willReturn(getMockAssembleia());
		
		mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
		
	}
	
	public Assembleia getMockAssembleia() {
		
		Assembleia assembleia = new Assembleia();
		assembleia.setCodAssembleia(COD_ASSEMBLEIA);
		assembleia.setTitulo(TITULO);
		assembleia.setDescricao(DESCRICAO);
		assembleia.setDataAssembleia(DATA_ASSEMBLEIA);
		assembleia.setDataCriacao(DATA_CRIACAO);
		assembleia.setListaPautas(LISTA_PAUTAS);
		
		return assembleia;
	}
	
	public String getJsonPayload() throws JsonProcessingException {
		
		AssembleiaDTO dto = new AssembleiaDTO();
		
		dto.setCodAssembleia(COD_ASSEMBLEIA);
		dto.setTitulo(TITULO);
		dto.setDescricao(DESCRICAO);
		dto.setDataAssembleia(DATA_ASSEMBLEIA);
		dto.setDataCriacao(DATA_CRIACAO);
		dto.setListaPautas(LISTA_PAUTAS);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper.writeValueAsString(dto);
	}
}
