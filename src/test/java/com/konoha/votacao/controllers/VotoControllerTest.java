package com.konoha.votacao.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.konoha.votacao.controllers.forms.VotoForm;
import com.konoha.votacao.exceptions.VotoException;
import com.konoha.votacao.services.VotoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VotoControllerTest {
	
	@Autowired
	private MockMvc mvc;
	@MockBean
	private VotoService votoService;
	
	private VotoForm votoForm;
	
	@Before
	public void setUp() {
		votoForm = new VotoForm();
		votoForm.setVoto(true);
		votoForm.setItemPautaId(1L);
	}
	
	@Test
	public void testSalvaVoto() throws Exception {
		doNothing().when(votoService).salvarVoto(votoForm.getItemPautaId(), votoForm.getVoto());
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)))		
				.andExpect(status().isCreated());
	}
	
	@Test
	public void testSalvaVotoNulo() throws Exception {
		votoForm.setItemPautaId(null);
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)))		
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testSalvaVotoItemPautaNulo() throws Exception {
		votoForm.setItemPautaId(null);
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)))		
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testSalvaVotoItemPautaNaoExiste() throws Exception {
		doThrow(VotoException.class).when(votoService)
			.salvarVoto(votoForm.getItemPautaId(), votoForm.getVoto());
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)))		
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testSalvaVotoRepetido() throws Exception {
		doThrow(VotoException.class).when(votoService)
			.salvarVoto(votoForm.getItemPautaId(), votoForm.getVoto());
		mvc.perform(MockMvcRequestBuilders.post("/votos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(votoForm)));
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
