package com.vacdnd.tools.controller;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.vacdnd.tools.domain.AuthToken;

@Component
public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {

}
