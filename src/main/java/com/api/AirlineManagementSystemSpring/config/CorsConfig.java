package com.api.AirlineManagementSystemSpring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			
			 @Override
			 public void addCorsMappings(CorsRegistry registry) {
				 registry.addMapping("/**")
				 		 .allowedOrigins(
							"http://airline-system-interface:80",
							"http://localhost:9999",
							"http://ec2-3-147-2-138.us-east-2.compute.amazonaws.com",
							"http://ec2-3-147-2-138.us-east-2.compute.amazonaws.com:80",
							"http://ec2-3-147-2-138.us-east-2.compute.amazonaws.com:4200",
							"http://ec2-3-147-2-138.us-east-2.compute.amazonaws.com:9999" 
						 )
				 		 .allowedMethods("GET", "POST", "PUT", "DELETE")
				 		 .allowedHeaders("*");
			 }
		};
	}
	
}
