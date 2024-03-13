package com.vacdnd.tools.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.vacdnd.tools.domain.User;

@Component
public interface UserRepository extends CrudRepository<User, Long>{
	
	@Query(value = "SELECT * FROM User WHERE email = ?1", nativeQuery = true)
	Optional<User> getUserByEmail(String email);
}
