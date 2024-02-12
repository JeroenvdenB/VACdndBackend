package com.vacdnd.tools.dto;

public class UserRegistrationDto {
	/**
	 * Newly registering users, as well as user that update their password, require a registration key.
	 * This object contains all User and RegistrationKey attributes (save for id, which is generated).
	 */
	
	private String email;
	private String password;
	private String registrationKey;
	
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
	public String getRegistrationKey() {
		return registrationKey;
	}
	public void setRegistrationKey(String registrationKey) {
		this.registrationKey = registrationKey;
	}
}
