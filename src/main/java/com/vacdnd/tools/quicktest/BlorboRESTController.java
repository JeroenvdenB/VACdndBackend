package com.vacdnd.tools.quicktest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlorboRESTController {
	@Autowired
	BlorboRepository blorboRepository;
	
	@GetMapping("blorbo")
	public String testEndpoint() {
		Blorbo blorbo = new Blorbo();
		blorbo.setName("Friend-shaped");
		blorboRepository.save(blorbo);
		
		System.out.println("Blorbo was called successfully. It is friend-shaped!");
		
		return "the blorbo endpoint worked and saved a db entry";
	}
}
