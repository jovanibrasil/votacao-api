package com.konoha.votacao.configs.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.konoha.votacao.modelo.Usuario;
import com.konoha.votacao.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AutenticacaoTokenFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private final TokenService tokenService;
	private final UsuarioRepository usuarioRepository;
	
	/**
	 * Intercepta as requisições que chegam e busca no cabeçalho pelo header 
	 * de autenticação. O token contido neste header é verificado e a devida
	 * autenticação é realizada. 
	 * 
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader(AUTHORIZATION_HEADER);
		if(token != null && !token.isEmpty() && token.startsWith("Bearer ")) {
			token = token.substring(7, token.length());
			if(tokenService.isValid(token)) {
				Optional<Usuario> optUsuario = usuarioRepository.findById(tokenService.getIdUsuario(token));
				if(optUsuario.isPresent()) {
					SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(optUsuario.get(), 
							null, optUsuario.get().getAuthorities()));
				}else {
					log.info("Usuário especificado no token não foi encontrado.");
				}
			}else {
				log.info("Tentativa de autenticação com token inválido.");
			}
		}else {
			log.info("Requisição não possui token.");
		}
		
		
		filterChain.doFilter(request, response);
	}
	
}
