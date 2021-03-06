package com.konoha.votacao.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.konoha.votacao.configs.security.TokenService;
import com.konoha.votacao.dto.AssembleiaDTO;
import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.forms.AtualizarAssembleiaForm;
import com.konoha.votacao.mappers.AssembleiaMapper;
import com.konoha.votacao.modelo.Assembleia;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.modelo.Perfil;
import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.repository.AssembleiaRepository;
import com.konoha.votacao.repository.UsuarioRepository;
import com.konoha.votacao.services.AssembleiaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AssembleiaControllerTest {

	private static final Long ASSEMBLEIA_ID = 1L;
	private static final String TITULO = "Aumento de investimento";
	private static final String DESCRICAO = "Aumento do investimento agrário";
	private static final LocalDateTime DATA_ASSEMBLEIA = LocalDateTime.of(2020, 03, 30, 20, 0);
	private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
	private static final List<Pauta> LISTA_PAUTAS = null;
	private static final String URL = "/assembleias";

	private AssembleiaDTO assembleiaDTO;
	private Assembleia assembleia;
	private AtualizarAssembleiaForm atualizarAssembleiaForm;
	
	private final String AUTH_HEADER = "Authorization";
	private final String AUTH_HEADER_CONTENT = "Bearer x.x.x.x";

	@MockBean
	private AssembleiaService assembleiaService;
	@MockBean
	private AssembleiaMapper assembleiaMapper;
	@MockBean
	private TokenService tokenService;
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AssembleiaRepository assembleiaRepository;

	@Autowired
	MockMvc mvc;

	@Before
	public void setUp() {
		assembleiaRepository.deleteAll();

		assembleia = new Assembleia();
		assembleia.setId(ASSEMBLEIA_ID);
		assembleia.setTitulo(TITULO);
		assembleia.setDescricao(DESCRICAO);
		assembleia.setDataAssembleia(DATA_ASSEMBLEIA);
		assembleia.setDataCriacao(DATA_CRIACAO);
		assembleia.setListaPautas(LISTA_PAUTAS);
		assembleiaRepository.save(assembleia);

		assembleiaDTO = new AssembleiaDTO();

		assembleiaDTO.setId(ASSEMBLEIA_ID);
		assembleiaDTO.setTitulo(TITULO);
		assembleiaDTO.setDescricao(DESCRICAO);
		assembleiaDTO.setDataAssembleia(DATA_ASSEMBLEIA);
		assembleiaDTO.setDataCriacao(DATA_CRIACAO);
		
		atualizarAssembleiaForm = new AtualizarAssembleiaForm();    
	    atualizarAssembleiaForm.setTitulo("update teste unitário");
	    atualizarAssembleiaForm.setDescricao("update teste unitário");
	    atualizarAssembleiaForm.setDataAssembleia(DATA_ASSEMBLEIA);
		
		Usuario usuario = new Usuario();
		usuario.setCpf("10879065044");
		Perfil perfil = new Perfil();
		perfil.setName("ROLE_ADMIN");
		usuario.setPerfis(Arrays.asList(perfil));
		when(tokenService.isValid(any())).thenReturn(true);
		when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));

	}

	/**
	 * Testa a operação de criação de uma assembleia.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSave() throws Exception {
		when(assembleiaMapper.assembleiaDtoToAssembleia(assembleiaDTO)).thenReturn(assembleia);
		when(assembleiaService.save(any())).thenReturn(assembleia);
		mvc.perform(MockMvcRequestBuilders.post(URL)
				.header(AUTH_HEADER, AUTH_HEADER_CONTENT)
				.content(asJsonString(assembleia))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

	}

	/**
	 * Testa a operação de criação de uma assembleia, porém a assembleia é inválida
	 * não possuindo título.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostAssembeliaTituloNull() throws Exception {
		assembleiaDTO.setTitulo(null);

		mvc.perform(MockMvcRequestBuilders.post(URL)
				.header(AUTH_HEADER, AUTH_HEADER_CONTENT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(assembleiaDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	/**
	 * Testa a operação de criação de uma assembleia, porém a assembleia é inválida
	 * devido ao título ser apenas uma string vazia.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostAssembeliaTituloVazio() throws Exception {
		assembleiaDTO.setTitulo("              ");

		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header(AUTH_HEADER, AUTH_HEADER_CONTENT)
				.content(asJsonString(assembleiaDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	/**
	 * Testa uma operação de busca por ID, correto.
	 * 
	 * @throws Exception
	 */
	@Test
	public void buscarIdValido() throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
        configJsonProvider(objectMapper);

		assembleiaDTO.setId(ASSEMBLEIA_ID);
		when(assembleiaService.findById(ASSEMBLEIA_ID)).thenReturn(assembleia);
		when(assembleiaMapper.assembleiaDtoToAssembleia(any())).thenReturn(assembleia);
		when(assembleiaMapper.assembleiaToAssembleiaDto(any())).thenReturn(assembleiaDTO);

		mvc.perform(MockMvcRequestBuilders.get(URL + "/1")
				.header(AUTH_HEADER, AUTH_HEADER_CONTENT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1L)));

		verify(assembleiaService, times(1)).findById(ASSEMBLEIA_ID);
	}

	/**
	 * Testa uma operação de busca por ID, incorreto
	 * 
	 * @throws Exception
	 */
	@Test
	public void buscarIdInvalido() throws Exception {

		when(assembleiaMapper.assembleiaDtoToAssembleia(assembleiaDTO)).thenReturn(assembleia);
		when(assembleiaMapper.assembleiaToAssembleiaDto(assembleia)).thenReturn(assembleiaDTO);
		when(assembleiaService.findById(any())).thenThrow(NotFoundException.class);

		mvc.perform(MockMvcRequestBuilders.get(URL + "/99")
				.contentType(MediaType.APPLICATION_JSON)
				.header(AUTH_HEADER, AUTH_HEADER_CONTENT))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	/**
	 * Teste busca de uma lista de assembleia
	 * 
	 * @throws Exception
	 */
	@Test
	public void testListaAssembeliasExistente() throws Exception {
		Assembleia assembleia = new Assembleia();
		AssembleiaDTO dto = new AssembleiaDTO();
		when(assembleiaMapper.assembleiaDtoToAssembleia(dto)).thenReturn(assembleia);
		when(assembleiaService.findAll(any())).thenReturn(new PageImpl<Assembleia>(Arrays.asList(assembleia)));

		mvc.perform(MockMvcRequestBuilders.get(URL)
			.header(AUTH_HEADER, AUTH_HEADER_CONTENT))
			.andExpect(status().isOk());
	}
	
	/**
	 * Testa remoção da assembleia por ID
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeletarAssembleia() throws Exception {
		doNothing().when(assembleiaService).deleteById(ASSEMBLEIA_ID);		
		mvc.perform(MockMvcRequestBuilders.delete("/assembleias/" + ASSEMBLEIA_ID)
			.header(AUTH_HEADER, AUTH_HEADER_CONTENT))						
			.andExpect(status().isNoContent());
	}
	
	/**
	 * Testa remoção da assembleia por ID inválido
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeletarAssembleiaIdInvalido() throws Exception {
		doThrow(NotFoundException.class).when(assembleiaService).deleteById(Long.parseLong("0000"));		
		mvc.perform(MockMvcRequestBuilders.delete("/assembleias/" + "0000")				
			.header(AUTH_HEADER, AUTH_HEADER_CONTENT))						
			.andExpect(status().isNotFound());
	}
	
	/**
   * Testa a operação de alterar uma assembleia
   * 
   * @throws Exception
   */
  
  @Test
  public void testAleterarAssembleia() throws Exception {
    
    when(assembleiaMapper.atualizarAssembleiaFormToAssembleia(any()))
       .thenReturn(assembleia);
     
    when(assembleiaService.atualizar(assembleia)).thenReturn(assembleia);   
    when(assembleiaMapper.assembleiaToAssembleiaDto(assembleia)).thenReturn(assembleiaDTO);

    mvc.perform(MockMvcRequestBuilders.patch("/assembleias/1")
        .header(AUTH_HEADER, AUTH_HEADER_CONTENT)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(atualizarAssembleiaForm))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        
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
