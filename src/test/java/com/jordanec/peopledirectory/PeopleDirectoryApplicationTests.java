package com.jordanec.peopledirectory;

import static org.junit.Assert.*;
import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.repository.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PeopleDirectoryApplicationTests {
	@Autowired
	private PersonRepository personRepository;
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void populateDB() {
		try {
			File persons_json = new ClassPathResource("data.json").getFile();
	    	List<Person> persons = objectMapper.readValue(persons_json, objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
	    	assertNotNull(personRepository.save(persons));
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//@Test
	public void removeDB() {
		personRepository.deleteAll();
	}
	
}
