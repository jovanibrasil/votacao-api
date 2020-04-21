package com.konoha.votacao.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Perfil implements GrantedAuthority {

	private static final long serialVersionUID = 4086271331381161523L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	
	@Override
	public String getAuthority() {
		return this.name;
	}
	
}
