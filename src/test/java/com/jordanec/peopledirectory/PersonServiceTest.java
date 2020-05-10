package com.jordanec.peopledirectory;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.service.PersonService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {	
	@Autowired
	private PersonService personService;
	private static ObjectMapper objectMapper = new ObjectMapper();
	private final Logger logger = LoggerFactory.getLogger(PersonIntegrationTest.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	
	@Test
	public void testGetPerson() {
		String id = "10";
		logger.info("testGetPerson("+id+"): ");
		Optional<Person> optionalPerson = personService.getById(id);
		assertTrue(optionalPerson.isPresent());
		logger.info(optionalPerson.get().toString());
	}
	@Test
	public void  testPersonExistsPerson()
	{
		String id = "15";
		Optional<Person> optionalPerson = personService.getById(id);
		assertTrue(optionalPerson.isPresent());
		logger.info("testPersonExists(person: "+optionalPerson.get().getFirstName() +")");
		assertTrue(personService.exists(optionalPerson.get()));
	}
	@Test
	public void  testPersonExistsInt(){
		String id = "25";
		logger.info("testPersonExists("+id+")");
		assertTrue(personService.existsById(id));
	}
	@Test
	public void  testCountPersons(){
		logger.info("testCountPersons()");
		long count = personService.count();
		assertThat(count, not(0));
		logger.info("count: "+count);
	}
	@Test
	public void  testGetPersons(){
		logger.info("testGetPersons()");
		int size = personService.findAll().size();
		assertNotEquals(0, size);
		logger.info("size: "+size);
	}
	@Test
	public void  testFindBornBetween(){
		try {
			String s = "01/01/1980";
			String e = "31/12/1980";

			LocalDate start = LocalDate.parse("01/01/1980");
			LocalDate end = LocalDate.parse("31/12/1980");
			logger.info("testFindBornBetween(\""+s+"\", \""+e+"\")");
			List<Person> persons = personService.findBornBetween(start, end);
			assertNotNull(persons);
			
			for (Person person : persons) {
				logger.info("person: id: "+person.getId()+", DateOfBirth: "+dateFormat.format(person.getDateOfBirth()));

			assertThat(person.getDateOfBirth().compareTo(start), greaterThanOrEqualTo(0));
				assertThat(person.getDateOfBirth().compareTo(end), lessThanOrEqualTo(0));
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// TODO
	@Test
	public void  testCreatePersons(/*List<Person> persons*/){
		logger.info("testCreatePersons()");
	}
	@Test
	public void testCreatePerson(/*Person person*/){
		logger.info("testCreatePerson()");
	}
	@Test
	public void testFindByFirstNameLike(/*String q*/){
		logger.info("testFindByFirstNameLike()");
	}
	@Test
	public void testReadAllByIdNotNullOrderByIdDesc(){
		logger.info("testReadAllByIdNotNullOrderByIdDesc()");
	}
	
	@Test
	public void testFindByLastNameAndFirstNameAllIgnoreCase(/*String lastname, String firstname*/){
		logger.info("testFindByLastNameAndFirstNameAllIgnoreCase()");
	}
	@Test
	public void testFindByLastNameOrFirstNameAllIgnoreCase(/*String lastname, String firstname*/){
		logger.info("testFindByLastNameOrFirstNameAllIgnoreCase()");
	}
	@Test
	public void testCountAllByIdNotNull(){
		logger.info("testCountAllByIdNotNull()");
	}
	@Test
	public void testFindByDateOfBirthBetweenOrderById(/*Date start, Date end*/){
		logger.info("testFindByDateOfBirthBetweenOrderById()");
	}
	@Test
	public void testFindByMobileBetween(/*long start, long end*/){
		logger.info("testFindByMobileBetween()");
	}
	@Test
	public void testFindByGender(/*String gender*/){
		logger.info("testFindByGender()");
	}
	@Test
	public void testFindDistinctPeopleByCountryIgnoreCase(/*String country*/){
		logger.info("testFindDistinctPeopleByCountryIgnoreCase()");
	}
}
