package com.konoha.votacao.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.konoha.votacao.controllers.forms.ItemPautaForm;
import com.konoha.votacao.dto.ItemPautaDTO;
import com.konoha.votacao.exceptions.NotFoundException;
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
	private ItemPautaDTO itemPautaDto;

	private final String DESCRICAO = "Descrição";
	private final String TITULO = "Título";
	private final Long PAUTA_ID = 2L;
	private final long COD_ITEM_PAUTA = 49;
	private final LocalDateTime CREATION_DATE = LocalDateTime.now();

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

		itemPautaDto = new ItemPautaDTO();
		itemPautaDto.setId(COD_ITEM_PAUTA);
		itemPautaDto.setTitulo(TITULO);
		itemPautaDto.setDescricao(DESCRICAO);
		itemPautaDto.setDataCriacao(CREATION_DATE);
	}

	/**
	 * Testa a operação de criação de item pauta com um item de pauta válido.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPauta() throws Exception {
		when(itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm)).thenReturn(itemPauta);
		when(itemPautaService.save(any())).thenReturn(itemPauta);

		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2/itens")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(itemPautaForm)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("/assembleias/12/pautas/2/itens/49")))
				.andExpect(jsonPath("$").doesNotExist());
	}

	/**
	 * Testa a operação de criação de item de pauta com um item de pauta inválido,
	 * com título null.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloNull() throws Exception {
		itemPautaForm.setTitulo(null);
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2/itens")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(itemPautaForm)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}

	/**
	 * Testa a operação de criação de item de pauta com um item de pauta inválido,
	 * com título em branco.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTitleEmBranco() throws Exception {
		itemPautaForm.setTitulo("          ");
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2/itens")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(itemPautaForm)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}

	/**
	 * Testa a operação de criação de item de pauta com um item de pauta inválido,
	 * com título tendo menos caracteres que o mínimo necessário.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloMenorQueTamanhoMinimo() throws Exception {
		itemPautaForm.setTitulo("abcd");
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2/itens")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(itemPautaForm)))
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}

	/**
	 * Testa a operação de criação de item de pauta com um item de pauta inválido,
	 * com título tendo mais caracteres que o máximo possível.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostItemPautaTituloMaiorQueTamanhoMaximo() throws Exception {
		itemPautaForm.setTitulo("abcd");
		mvc.perform(MockMvcRequestBuilders.post("/assembleias/12/pautas/2/itens")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(itemPautaForm)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}

	/**
	 * Testa a busca de um item de pauta pelo seu ID. Neste caso temos um ID de um
	 * item válido, ou seja, está registrado no banco. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetItemById() throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
        configJsonProvider(objectMapper);

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
		String formatedDate = CREATION_DATE.format(dateTimeFormatter);

		when(itemPautaMapper.itemPautaToItemPautaDTO(itemPauta)).thenReturn(itemPautaDto);
		when(itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm)).thenReturn(itemPauta);
		when(itemPautaService.findById(any())).thenReturn(itemPauta);
		mvc.perform(MockMvcRequestBuilders.get("/assembleias/12/pautas/2/itens/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.errors").isEmpty())
				.andExpect(jsonPath("$.data.id", equalTo(COD_ITEM_PAUTA)))
				.andExpect(jsonPath("$.data.titulo", equalTo(TITULO)))
				.andExpect(jsonPath("$.data.descricao", equalTo(DESCRICAO)))
				.andExpect(jsonPath("$.data.dataCriacao", equalTo(formatedDate)));
	}

	/**
	 * Testa a busca de de um item de pauta por ID, porém este ID é inválido, uma vez
	 * que não existe registro de item com o mesmo no sistema.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetItemNaoExistenteById() throws Exception {
		when(itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm)).thenReturn(itemPauta);
		when(itemPautaMapper.itemPautaToItemPautaDTO(itemPauta)).thenReturn(itemPautaDto);
		when(itemPautaService.findById(any())).thenThrow(NotFoundException.class);

		mvc.perform(MockMvcRequestBuilders.get("/assembleias/12/pautas/2/itens/99")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
	}

	/**
	 * Testa a listagem de itens de uma pauta existente no sistema.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testListaItensDeUmaPautaExistente() throws Exception {
		when(itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm)).thenReturn(itemPauta);
		when(itemPautaService.findByPautaId(any(), any()))
				.thenReturn(new PageImpl<ItemPauta>(Arrays.asList(itemPauta, itemPauta, itemPauta)));

		mvc.perform(MockMvcRequestBuilders.get("/assembleias/12/pautas/2/itens"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	/**
	 * Testa a tentativa de listagem de itens de uma pauta não existente no sistema.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testListaItensDeUmaPautaNaoExistente() throws Exception {
		when(itemPautaMapper.itemPautaFormToItemPauta(itemPautaForm)).thenReturn(itemPauta);
		when(itemPautaService.findByPautaId(any(), any())).thenThrow(NotFoundException.class);

		mvc.perform(MockMvcRequestBuilders.get("/assembleias/12/pautas/2/itens"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isNotEmpty());
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

	private void configJsonProvider(ObjectMapper objectMapper) {

		Configuration.setDefaults(new Configuration.Defaults() {

			private final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
			private final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);

			@Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}

			@Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}

			@Override
			public Set<Option> options() {
				return EnumSet.noneOf(Option.class);
			}
		});
	}

}
