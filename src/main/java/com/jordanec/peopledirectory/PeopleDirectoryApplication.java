package com.jordanec.peopledirectory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.jordanec.peopledirectory.controller.PersonController;

@SpringBootApplication
public class PeopleDirectoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeopleDirectoryApplication.class, args);
	}
	
}
