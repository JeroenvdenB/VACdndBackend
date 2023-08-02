package com.vacdnd.tools.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class propertyTest {
	@Autowired
	private Environment environment;
	
	public void printProperty() {
		String server = environment.getProperty("server.port");	
		System.out.println(server);
	}
}
