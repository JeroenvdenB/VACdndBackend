package com.vacdnd.tools.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.vacdnd.tools.util.TableFormatter.formatTable;

@RestController
public class FormattersEndpoint {
	
	@PostMapping("echo")
	public String echo(@RequestBody String input) {
		System.out.println("Echo endpoint recieved: " + input);
		return input;
	}
	
	@PostMapping("formatTable")
	public String formatTable(@RequestBody String input) {
		
		try {
			String niceTable = formatTable(input);
			return niceTable;
		} catch (Exception e){
			return "Something went wrong!";
		}
	}
	
}
