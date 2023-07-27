package com.vacdnd.tools.quicktest;

import org.springframework.stereotype.Component;
import org.springframework.data.repository.CrudRepository;

@Component
public interface BlorboRepository extends CrudRepository<Blorbo, Long> {
	
}
