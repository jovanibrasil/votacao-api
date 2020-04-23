package com.konoha.votacao.controllers;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.konoha.votacao.configs.security.TokenService;
import com.konoha.votacao.forms.LoginForm;
import com.konoha.votacao.modelo.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AutenticacaoControllerTest {

	@MockBean
	private AuthenticationManager authManager;
	@MockBean
	private TokenService tokenService;
	@Autowired
	private MockMvc mvc;

	private LoginForm loginForm;
	private Usuario usuario;

	@Before
	public void setUp() {
		loginForm = new LoginForm();
		loginForm.setSenha("123456");
		loginForm.setCpf("10879065044");
		
		usuario = new Usuario();
		usuario.setCpf(loginForm.getCpf());
		usuario.setSenha(loginForm.getSenha());
	}

	/**
	 * Testa autenticação com cpf inválido.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAutenticacaoCpfInvalido() throws Exception {
		loginForm.setCpf("00000000000");
		mvc.perform(MockMvcRequestBuilders.post("/auth")
		        .accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(loginForm))
		        .contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isBadRequest());
	}
	
	/**
	 * Testa autenticação válida.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAutenticacaoValida() throws Exception {
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authManager.authenticate(any())).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(usuario);
		when(tokenService.gerarToken(usuario)).thenReturn("TOKEN");
		mvc.perform(MockMvcRequestBuilders.post("/auth")
		        .accept(MediaType.APPLICATION_JSON)
		        .content(asJsonString(loginForm))
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

}
