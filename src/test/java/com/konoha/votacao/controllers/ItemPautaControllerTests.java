package com.konoha.votacao.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.konoha.votacao.controllers.forms.ItemPautaForm;
import com.konoha.votacao.mappers.ItemPautaMapper;
import com.konoha.votacao.modelo.ItemPauta;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.services.ItemPautaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ItemPautaControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ItemPautaService itemPautaService;
	@MockBean
	private ItemPautaMapper itemPautaMapper;
	
	private ItemPautaForm itemPautaForm;
	private ItemPauta itemPauta;
	
	private final String DESCRICAO = "Descrição";
	private final String TITULO = "Título";
	private final long PAUTA_ID = 2L;
	private final long COD_ITEM_PAUTA = 49L;
	
	@Before
	public void setUp() {
		itemPautaForm = new ItemPautaForm();
		itemPautaForm.setDescricao(DESCRICAO);
		itemPautaForm.setTitulo(TITULO);
		itemPautaForm.setPautaId(PAUTA_ID);
	
		itemPauta = new ItemPauta();
		itemPauta.setDescricao("Descrição");
		itemPauta.setTitulo("Título");
		itemPauta.setCodItemPauta(COD_ITEM_PAUTA);
		Pauta pauta = new Pauta();
		pauta.setCodPauta(PAUTA_ID);
		itemPauta.setPauta(pauta);
	}
	
	/**
	 * Testa a operação de criação de item pauta com um item
	 * de pauta válido.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPauta() throws Exception {
		when(itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm)).thenReturn(itemPauta);
		when(itemPautaService.save(any())).thenReturn(itemPauta);
		
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(itemPautaForm)))		
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", 
						containsString("/assembleias/12/pautas/2/itens/49")))
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	/**
	 * Testa a operação de criação de item de pauta com um item 
	 * de pauta inválido, com título null.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloNull() throws Exception {
		itemPautaForm.setTitulo(null);
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(itemPautaForm)))		
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	/**
	 * Testa a operação de criação de item de pauta com um item 
	 * de pauta inválido, com título em branco.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTitleEmBranco() throws Exception {
		itemPautaForm.setTitulo("          ");
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(itemPautaForm)))		
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	/**
	 * Testa a operação de criação de item de pauta com um item 
	 * de pauta inválido, com título tendo menos caracteres que o
	 * mínimo necessário.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloMenorQueTamanhoMinimo() throws Exception {
		itemPautaForm.setTitulo("abcd");
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(itemPautaForm)))		
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	/**
	 * Testa a operação de criação de item de pauta com um item 
	 * de pauta inválido, com título tendo mais caracteres que o
	 * máximo possível.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloMaiorQueTamanhoMaximo() throws Exception {
		itemPautaForm.setTitulo("abcd");
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(itemPautaForm)))		
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").doesNotExist());
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
