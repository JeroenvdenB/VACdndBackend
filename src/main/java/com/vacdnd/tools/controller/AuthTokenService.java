package com.vacdnd.tools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vacdnd.tools.domain.AuthToken;

@Service
public class AuthTokenService {
	@Autowired
	AuthTokenRepository authTokenRepository;
	
	public void saveAuthToken(AuthToken token) {
		authTokenRepository.save(token);
	}
}
