package com.konoha.votacao.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.konoha.votacao.controllers.forms.VotoForm;
import com.konoha.votacao.dto.VotoDTO;
import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.exceptions.VotoException;
import com.konoha.votacao.mappers.VotoMapper;
import com.konoha.votacao.services.VotoService;
import com.konoha.votacao.services.impl.modelos.ResultadoItemPauta;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VotoControllerTest {
	
	@Autowired
	private MockMvc mvc;
	@MockBean
	private VotoService votoService;
	@MockBean
	private VotoMapper votoMapper;
	
	private VotoForm votoForm;
	
	@Before
	public void setUp() {
		votoForm = new VotoForm();
		votoForm.setVoto(true);
		votoForm.setItemPautaId(1L);
	}
	
	/**
	 * Testa registro de um voto válido.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSalvaVoto() throws Exception {
		doNothing().when(votoService).saveVoto(votoForm.getItemPautaId(), votoForm.getVoto());
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)))		
				.andExpect(status().isCreated());
	}
	
	/**
	 * Testa registro de um voto inválido (nulo)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSalvaVotoNulo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(null)))		
				.andExpect(status().isBadRequest());
	}

	/**
	 * Testa registro de um voto inválido (item de pauta inválido)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSalvaVotoItemPautaNulo() throws Exception {
		votoForm.setItemPautaId(null);
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)))		
				.andExpect(status().isBadRequest());
	}
	
	/**
	 * Testa registro de um voto inválido (item de pauta não existe)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSalvaVotoItemPautaNaoExiste() throws Exception {
		doThrow(VotoException.class).when(votoService)
			.saveVoto(votoForm.getItemPautaId(), votoForm.getVoto());
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)))		
				.andExpect(status().isBadRequest());
	}
	
	/**
	 * Testa registro de um voto inválido (voto já foi feito)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSalvaVotoRepetido() throws Exception {
		doThrow(VotoException.class).when(votoService)
			.saveVoto(votoForm.getItemPautaId(), votoForm.getVoto());
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)));
	}
	
	/*
	 * Busca votos de uma pauta existente e aberta.
	 * 
	 */
	@Test
	public void testBuscaVotosItensPauta() throws Exception {
		
		ResultadoItemPauta r1 = new ResultadoItemPauta(1L);
		ResultadoItemPauta r2 = new ResultadoItemPauta(1L);
		r1.addVotoContrario();
		r2.addVotoFavoravel();
		
		List<ResultadoItemPauta> resultados = Arrays.asList(r1, r2);
		when(votoService.findResultadoVotacaoByPautaId(1L)).thenReturn(resultados);
		when(votoMapper.resultadoItemPautaToVotoDto(any())).then(new Answer<VotoDTO>() {
			@Override
			public VotoDTO answer(InvocationOnMock invocation) throws Throwable {
				ResultadoItemPauta r = (ResultadoItemPauta) invocation.getArgument(0);
				VotoDTO voto = new VotoDTO();
				voto.setVotosContrarios(r.getVotosContrarios());
				voto.setVotosFavoraveis(r.getVotosFavoraveis());
				return voto;
			}
		});
		
		mvc.perform(MockMvcRequestBuilders.get("/votos/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	/*
	 * Busca votos de uma pauta existente mas fechada.
	 * 
	 */
	@Test
	public void testBuscaVotosItensPautaFechada() throws Exception {
		
		when(votoService.findResultadoVotacaoByPautaId(1L)).thenThrow(VotoException.class);
		
		mvc.perform(MockMvcRequestBuilders.get("/votos/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	/*
	 * Busca votos de uma pauta inexistente.
	 * 
	 */
	@Test
	public void testBuscaVotosItensPautaInexistente() throws Exception {
		
		when(votoService.findResultadoVotacaoByPautaId(1L)).thenThrow(NotFoundException.class);
		
		mvc.perform(MockMvcRequestBuilders.get("/votos/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
