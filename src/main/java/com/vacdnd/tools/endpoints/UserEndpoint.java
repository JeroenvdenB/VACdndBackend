package com.vacdnd.tools.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vacdnd.tools.controller.RegistrationKeyService;
import com.vacdnd.tools.domain.RegistrationKey;
import com.vacdnd.tools.controller.UserService;
import com.vacdnd.tools.domain.User;
import com.vacdnd.tools.dto.UserRegistrationDto;

@RestController
public class UserEndpoint {
	@Autowired
	UserService userService;
	
	@Autowired
	RegistrationKeyService registrationKeyService;
	
	@PostMapping("login")
	public boolean login(@RequestBody User toCheck) {
		
		// TODO account for no users found by that email. Raise error 204 - no content
		
		User user = userService.getUser(toCheck.getEmail()).iterator().next();
		boolean match = user.checkPassword(toCheck.getPassword());
		
		return match; //should I return strings so I can give more information?
	}
	
	@PostMapping("addNewUser")
	public String addNewUser(@RequestBody UserRegistrationDto userRegistrationDto) {
		
		
		// Extract User from DTo
		User user = new User();
		user.setEmail(userRegistrationDto.getEmail());
		user.setPassword(userRegistrationDto.getPassword());	
		
		// TODO check if email already exists. If yes, return with error.
		
		// Check the registration key is in the database. If it is, it's valid.
		boolean keyValidity = registrationKeyService.validateKey(userRegistrationDto.getRegistrationKey());
		
		if (keyValidity) {
			System.out.println("New user registred: " + user.getEmail());
			user.encrypt(); // Encrypt the password. Overwrites the plain-text password in User.		
			userService.saveUser(user);
			// TODO remove key from database; it's been used
			return "New user registred";
		} else {
			return "Invalid key";
		}
	}
}
