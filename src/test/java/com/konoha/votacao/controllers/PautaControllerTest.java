package com.konoha.votacao.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.konoha.votacao.controllers.forms.PautaForm;
import com.konoha.votacao.dto.PautaDTO;
import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.mappers.PautaMapper;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.services.PautaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PautaControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private PautaService pautaService;
	@MockBean
	private PautaMapper pautaMapper;
	
	private final String TITULO = "Título";
	private final String DESCRICAO = "Descricao";
	private final String OBSERVACOES = "Observações";
	private final Long ASSEMBLEIA_ID = 1L;
	private final Long PAUTA_ID = 49L;
	
	private PautaForm pautaForm;
	private Pauta pauta;
	private PautaDTO pautaDto;
	
	@Before
	public void setUp() {
		pautaForm = new PautaForm();
		pautaForm.setAssembleiaId(ASSEMBLEIA_ID);
		pautaForm.setDescricao(DESCRICAO);
		pautaForm.setObservacoes(OBSERVACOES);
		pautaForm.setTitulo(TITULO);
		
		pauta = new Pauta();
		pauta.setCodPauta(PAUTA_ID);		
	
		pautaDto = new PautaDTO();
		pautaDto.setCodPauta(pauta.getCodPauta());
		pautaDto.add(new Link("/"));
	}
	
	/**
	 * Testa a operação de criação de uma pauta
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPauta() throws Exception {
		when(pautaMapper.pautaFormToPauta(pautaForm)).thenReturn(pauta);
		when(pautaService.save(any())).thenReturn(pauta);
		
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(pautaForm)))		
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", 
						containsString("/assembleias/12/pautas/49")))
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	/**
	 * Testa a operação de criação de uma pauta, porém a pauta é
	 * inválida não possuindo título.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloNull() throws Exception {
		pautaForm.setTitulo(null);
		
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(pautaForm)))		
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}
	
	/**
	 * Testa a operação de criação de uma pauta, porém a pauta é
	 * inválida devido ao título ser apenas uma string vazia.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloVazio() throws Exception {
		pautaForm.setTitulo("              ");
		
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(pautaForm)))		
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}
	
	/**
	 * Testa a operação de criação de uma pauta, porém a pauta é
	 * inválida devido ao título ser maior que o tamanho máximo.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloMaiorQueTamanhoMaximo() throws Exception {
		pautaForm.setTitulo("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(pautaForm)))		
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}
	
	/**
	 * Testa a operação de criação de uma pauta, porém a pauta é
	 * inválida devido ao título ser menor que o tamanho mínimo.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloMenorQueTamanhoMinimo() throws Exception {
		pautaForm.setTitulo("v");
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(pautaForm)))		
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}
	
	/**
	 * Busca por ID uma pauta existente no sistema.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBuscaPautaPorId() throws Exception {
		when(pautaService.findById(1L)).thenReturn(pauta);
		when(pautaMapper.pautaToPautaDto(pauta, ASSEMBLEIA_ID)).thenReturn(pautaDto);
		
		mvc.perform(MockMvcRequestBuilders.get("/assembleias/" + ASSEMBLEIA_ID + "/pautas/1")
				.contentType(MediaType.APPLICATION_JSON))		
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$._links").isNotEmpty());
	}
	
	/**
	 * Busca por ID uma pauta não existente no sistema.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBuscaPautaPorIdInvalido() throws Exception {
		when(pautaService.findById(1L)).thenThrow(NotFoundException.class);
		
		mvc.perform(MockMvcRequestBuilders.get("/assembleias/12/pautas/1")
				.contentType(MediaType.APPLICATION_JSON))		
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}
	
	/**
	 * Testa a busca de pautas de uma assembleia.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBuscaPautasPorAssembleia() throws Exception {
		Page<Pauta> pautas = new PageImpl<>(Arrays.asList(pauta, pauta, pauta));
		when(pautaService.findByAssembleiaCodAssembleia(any(), any())).thenReturn(pautas);
		when(pautaMapper.pautaToPautaDto(pauta, ASSEMBLEIA_ID)).thenReturn(pautaDto);
		
		mvc.perform(MockMvcRequestBuilders.get("/assembleias/1/pautas")
				.contentType(MediaType.APPLICATION_JSON))		
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBuscaPautaPorAssembleiaNaoExistente() throws Exception {
		when(pautaService.findByAssembleiaCodAssembleia(any(), any()))
			.thenThrow(NotFoundException.class);
		
		mvc.perform(MockMvcRequestBuilders.get("/assembleias/1/pautas")
				.contentType(MediaType.APPLICATION_JSON))		
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors").isNotEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	/**
	 * Testa remoção da pauta por ID
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeletarPautaComSessaoExistente() throws Exception {
		doNothing().when(pautaService).deleteById(pauta.getCodPauta());		
		mvc.perform(MockMvcRequestBuilders.delete("/assembleias/12/pautas/1"))						
				.andExpect(status().isNoContent());
	}
	
	/**
	 * Testa remoção da pauta por ID invalido
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeletarPautaComSessaoExistenteIDInvalido() throws Exception {		
		doThrow(new NotFoundException("")).doNothing()
			.when(pautaService)
			.deleteById(PAUTA_ID);
		mvc.perform(MockMvcRequestBuilders.delete("/assembleias/12/pautas/"+PAUTA_ID))
			.andExpect(status().isNotFound());
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
