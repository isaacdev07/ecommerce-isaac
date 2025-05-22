package com.senai.ecommerce.entities;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "tb_role")
public class Role implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// classe especifica para definir cargos
	private String authoriry;
	
	
	

	public Role() {
	}

	public Role(Long id, String authoriry) {
		this.id = id;
		this.authoriry = authoriry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAuthoriry(String authoriry) {
		this.authoriry = authoriry;
	}

	@Override
	public String getAuthority() {
		return authoriry;
	}

}
