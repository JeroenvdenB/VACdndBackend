package com.vacdnd.tools.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5500", "https://www.vacdnd.com")
			.allowCredentials(true)
			.allowedMethods("POST", "GET", "OPTIONS", "PUT", "DELETE")
			.allowedHeaders("accept", "content-type");
	}
	
	// Note: if the origin is not explicitly present in the list, the header is 
	// simply not set. So the browser will report a *lack* of header, not a 
	// mismatching header.

}
