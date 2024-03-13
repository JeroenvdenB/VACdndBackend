package com.vacdnd.tools.domain;

import java.time.Instant;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.stream.IntStream;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class AuthToken {
	@SequenceGenerator(name="defSequence", allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
					generator="defSequence")
	private long id;
	
	@ManyToOne(optional=false)
	private User user;
	
	private String token;
	private Instant expiration; //does Instant work??
	
	public String generateToken() {
		SecureRandom secRand = new SecureRandom(); //equivalent to SecureRandom.getInstance("SHA1PRNG")
		int[] integers = secRand.ints(20, 45, 126).toArray();
		StringBuilder tokenBuilder = new StringBuilder();
		
		// I build my own String based on the integers given. Remove forbidden characters in cookie.
		// I start at 45 to cut off a bunch of symbols early in the ASCII table
		for (int i : integers) {
			switch (i) {
			case 59: //semicolon
			case 60: // <
			case 61: //equals
			case 62: // >
			case 92: //backslash
				i = i+5;
			}
			tokenBuilder.append((char)i);
		}
		return tokenBuilder.toString();
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Instant getExpiration() {
		return expiration;
	}
	public void setExpiration(Instant expiration) {
		this.expiration = expiration;
	}
	
}
