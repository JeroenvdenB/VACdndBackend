package com.vacdnd.tools.controller;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.vacdnd.tools.domain.User;

@Component
public interface UserRepository extends CrudRepository<User, Long>{

}
