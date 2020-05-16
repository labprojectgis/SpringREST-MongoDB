package com.jordanec.peopledirectory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class PeopleDirectoryApplication
{

	public static void main(String[] args) {
		SpringApplication.run(PeopleDirectoryApplication.class, args);
	}
	
}
