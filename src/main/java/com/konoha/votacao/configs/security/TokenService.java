package com.konoha.votacao.configs.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.konoha.votacao.modelo.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${votacao.jwt.expiration}")
	private String expiration;
	@Value("${votacao.jwt.secret}")
	private String secret;
	
	/**
	 * Gera um JWT assinado.
	 * 
	 * @param usuario
	 * @return
	 */
	public String gerarToken(Usuario usuario) {
		Date dataAtual = new Date();
		Date dataExpiracao = new Date(dataAtual.getTime() + Long.parseLong(expiration));
		return Jwts.builder()
				.setIssuer("votacao-api")
				.setSubject(usuario.getId().toString())
				.setIssuedAt(dataAtual)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	/**
	 * Verifica se um JWT é válido. U token é considerado inválido se ele tiver formato inválido, 
	 * estiver vencido ou com assinatura inválida.
	 * 
	 * @param token
	 * @return
	 */
	public boolean isValid(String token) {
		try {
			Jwts.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Faz a extração do usuário contido no JWT.
	 * 
	 * @param token
	 * @return
	 */
	public Long getIdUsuario(String token) {
		return Long.parseLong(Jwts.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getBody()
			.getSubject());
	}	
	
}
