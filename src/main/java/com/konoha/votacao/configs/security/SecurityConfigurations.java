package com.konoha.votacao.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.konoha.votacao.repository.UsuarioRepository;
import com.konoha.votacao.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;
	private final TokenService tokenService;
	
	/**
	 * Configura a autenticação. Indica para o spring qual o serviço que deve ser utilizado para 
	 * manipular dados dos usuários e qual o encoder deve ser utilizado.
	 * 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/**
	 * Configura a autorização.
	 * 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/votos").hasAnyRole("USER", "ADMIN") // Qualquer um (USER ou ADMIN) pode votor e buscar resultados de votação
			.antMatchers(HttpMethod.GET, "/assembleias/**").hasAnyRole("USER", "ADMIN") // Alterações 
			.antMatchers("/assembleias/**").hasRole("ADMIN") // Administrador pode modificar o que quiser 
			.antMatchers(HttpMethod.POST, "/auth").permitAll()
			.anyRequest().authenticated()
			.and()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilterBefore(new AutenticacaoTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
			.accessDeniedHandler(new JwtAccessDeniedHandler());	
	}
	
	/**
	 * Necessário injetar o AuthenticationManager como bean para que possamos
	 * usa-lo em outros lugares como no controller de autenticação.
	 * 
	 */
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
}
