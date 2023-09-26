package com.vacdnd.tools.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import at.favre.lib.crypto.bcrypt.BCrypt;


@Entity
public class User {
	@SequenceGenerator(name="defSequence", allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
					generator="defSequence")
	private long id;
	
	private String username;
	private String password;
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
