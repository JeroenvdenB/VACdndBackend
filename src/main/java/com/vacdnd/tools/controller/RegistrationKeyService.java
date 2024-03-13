package com.vacdnd.tools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vacdnd.tools.domain.RegistrationKey;

import java.util.Optional;

@Service
public class RegistrationKeyService {
	@Autowired
	RegistrationKeyRepository registrationKeyRepository;
	
	public boolean validateKey(String code) {
		// If any element is found, it's a match. That logic of matching to 'code' is performed with SQL.
		Optional<RegistrationKey> optionalKey = registrationKeyRepository.findByCode(code);
		boolean result = optionalKey.isPresent();
		
		// This key has now been used. It's deleted from the database immediately.
		// I count this as part of the key validation.
		// Doing it here is easier, since I don't return the whole key to the caller, and I need the id to delete it
		if (result) {
			RegistrationKey regKey = optionalKey.get();
			registrationKeyRepository.deleteById(regKey.getId());
			System.out.println("Key validated and removed from database");
		}
		
		return result;
	}

}
