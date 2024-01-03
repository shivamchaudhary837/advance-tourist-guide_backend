package com.capgemini.ocean.institute.training;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AdvancedTouristGuideUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedTouristGuideUserApplication.class, args);
	}

	     @Bean
	    public ModelMapper modelMapper() {
	        return new ModelMapper();
	    }
}
