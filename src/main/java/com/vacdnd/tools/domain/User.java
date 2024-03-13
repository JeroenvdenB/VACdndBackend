package com.vacdnd.tools.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Entity
public class User {
	@SequenceGenerator(name="defSequence", allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
					generator="defSequence")
	private long id;
	
	private String email;
	private String password;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<AuthToken> tokens;
	
	public void encrypt() {
		this.password = BCrypt.withDefaults().hashToString(14, this.password.toCharArray());
	}
	
	public boolean checkPassword(String password) {
		BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
		return result.verified;
	}
		
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public List<AuthToken> getTokens() {
		return tokens;
	}

	public void setTokens(List<AuthToken> tokens) {
		this.tokens = tokens;
	}
}
