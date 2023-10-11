package com.vacdnd.tools.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vacdnd.tools.controller.UserService;

@RestController
public class UserEndpoint {
	@Autowired
	UserService userService;
	
	@PostMapping("userAuth")
	public boolean userAuth() {
		System.out.println("userAuth was called");
		return true;
	}

}
