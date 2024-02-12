package com.vacdnd.tools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vacdnd.tools.domain.User;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public Iterable<User> getUser(String email) {
		return userRepository.getUser(email);
	}
	
}
