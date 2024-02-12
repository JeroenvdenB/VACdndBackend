package com.vacdnd.tools.controller;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.vacdnd.tools.domain.RegistrationKey;

@Component
public interface RegistrationKeyRepository extends CrudRepository<RegistrationKey, Long> {
	
	@Query(value = "SELECT * FROM Registration_key WHERE code = ?1", nativeQuery = true)
	List<RegistrationKey> getRegKey(String code);
}
