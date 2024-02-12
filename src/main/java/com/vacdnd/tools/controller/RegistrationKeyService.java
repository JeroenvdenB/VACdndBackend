package com.vacdnd.tools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vacdnd.tools.domain.RegistrationKey;

@Service
public class RegistrationKeyService {
	@Autowired
	RegistrationKeyRepository registrationKeyRepository;
	
	public boolean validateKey(String code) {
		Iterable<RegistrationKey> optionalKey = registrationKeyRepository.getRegKey(code);
		
		// If any element is found, it's a match. That logic was performed in SQL.
		if (optionalKey.iterator().hasNext()) {
			return true; 
		} else {
			return false;
		}		
	}
}
