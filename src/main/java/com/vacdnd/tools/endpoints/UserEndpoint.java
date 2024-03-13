package com.vacdnd.tools.endpoints;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vacdnd.tools.controller.AuthTokenService;
import com.vacdnd.tools.controller.RegistrationKeyService;
import com.vacdnd.tools.controller.UserService;
import com.vacdnd.tools.domain.User;
import com.vacdnd.tools.dto.UserRegistrationDto;
import com.vacdnd.tools.domain.AuthToken;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

@RestController
public class UserEndpoint {
	@Autowired
	UserService userService;
	
	@Autowired
	RegistrationKeyService registrationKeyService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	int tokenDuration = 1;
	ChronoUnit timeUnit = ChronoUnit.HOURS;
	
	// FOR TESTING - REMOVE LATER
	@GetMapping("readToken")
	public String readCookie(@CookieValue(value="session", defaultValue="noTokenFound") String token) {
		String text = "Your token is: " + token;
		System.out.println(text);
		return text;
	}
	
	@GetMapping("setCookie")
	public String setCookie(HttpServletResponse response) {
		//Create a cookie
		Cookie cookie = new Cookie("username", "Bob");
		
		//add cookie to response
		response.addCookie(cookie);
		
		return "A username cookie was set to Bob";
	}
	
	@GetMapping("setTokenCookie")
	public String setTokenCookie(HttpServletResponse response) {
		AuthToken authToken = new AuthToken();
		String token = authToken.generateToken();
		
		Cookie cookie = new Cookie("rememberme", token);
		response.addCookie(cookie);
		
		return "A token was set";
	}
	
	//TESTING ENDS HERE
	
	@PostMapping("login")
	public String login(HttpServletResponse response, @RequestBody User toCheck) {
		Optional <User> optionalUser = userService.getUserByEmail(toCheck.getEmail());
				
		// Confirm an account was retrieved.
		if (optionalUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No users found by that email");
		}
		
		// Grab User
		User user = optionalUser.get();
		
		// Check password. If there is no match, throw HttpStatus.FORBIDDEN
		if (!user.checkPassword(toCheck.getPassword())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password does not match email");			
		}
		
		// Generate a session token (authToken) for future API authorization
		AuthToken authToken = new AuthToken();
		String token = authToken.generateToken();
		authToken.setToken(token);
		authToken.setExpiration(Instant.now().plus(tokenDuration, timeUnit));
		authToken.setUser(user);
		authTokenService.saveAuthToken(authToken);
		System.out.println(token);
		System.out.println("authToken created and saved for user: " + user.getEmail());
		
		// Send the authToken as a cookie named 'session'
		Cookie session = new Cookie("session", token);
		session.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days <- should be same as token exp
		session.setHttpOnly(true);
		session.setAttribute("SameSite", "None");
		session.setSecure(true);
		
		response.addCookie(session);
		
		// Send a second cookie with the username (used for front-end display)
		Cookie username = new Cookie("username", user.getEmail());
		username.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days <- should be same as token exp
		
		response.addCookie(username);		
		
		return user.getEmail();
	}
	
	@PostMapping("addNewUser")
	public String addNewUser(@RequestBody UserRegistrationDto userRegistrationDto) {
		
		// Extract User from DTo
		User user = new User();
		user.setEmail(userRegistrationDto.getEmail());
		user.setPassword(userRegistrationDto.getPassword());
		
		// Check if email already exists. If yes, return with a message. 
		// HTTP status remains 200, as the request was received and processed fully.
		Optional<User> optionalUser = userService.getUserByEmail(user.getEmail());
		if (optionalUser.isPresent()) {
			return "This email is already in use!";
		}
		
		// Check the registration key is in the database. If it is, it's valid.
		boolean keyValidity = registrationKeyService.validateKey(userRegistrationDto.getRegistrationKey());
		
		if (keyValidity) {
			System.out.println("New user registred: " + user.getEmail());
			user.encrypt(); // Encrypt the password. Overwrites the plain-text password in User.		
			userService.saveUser(user);
			return "New user registred";
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid invite key");
		}
	}
}
