package com.jordanec.peopledirectory.service;

import com.jordanec.peopledirectory.PeopleDirectoryApplication;
import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PeopleDirectoryApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PersonServiceTest
{
    @Autowired
	private PersonService personService;
    @Autowired
	private PersonRepository personRepository;

	private final Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);
    private static final String NON_EXISTENT_PERSON_ID = "nonExistentPersonId";

    @Test
	public void getById_OK()
    {
        Person person = getRandomPerson();
        Optional<Person> optionalPerson = personService.getById(person.getId());
        assertTrue(optionalPerson.isPresent());
    }

    @Test
	public void getById_NotExists()
    {
        Optional<Person> optionalPerson = personService.getById(NON_EXISTENT_PERSON_ID);
        assertFalse(optionalPerson.isPresent());
    }

    @Test
	public void existsByDocument_OK()
    {
        Person person = getRandomPerson();
        assertTrue(personService.exists(person));
    }

    @Test
	public void existsById_OK()
    {
        Person person = getRandomPerson();
        assertTrue(personService.existsById(person.getId()));
    }

    @Test
	public void existsById_NotExists()
    {
        assertFalse(personService.existsById(NON_EXISTENT_PERSON_ID));
    }

    @Test
	public void count()
    {
        assertNotEquals(0, personService.count());
    }

    @Test
	public void findAll()
    {
        int size = personService.findAll().size();
        assertNotEquals(0, size);
    }

    @Test
	public void findBornBetween()
    {
        LocalDate startDate = LocalDate.parse("1990-01-01");
        LocalDate endDate = LocalDate.parse("1990-12-31");
        List<Person> persons = personService.findBornBetween(startDate, endDate);
        assertFalse(CollectionUtils.isEmpty(persons));
        for (Person person : persons)
        {
            assertThat(person.getDateOfBirth().compareTo(startDate), greaterThanOrEqualTo(0));
            assertThat(person.getDateOfBirth().compareTo(endDate), lessThanOrEqualTo(0));
        }
    }

	private Person getRandomPerson()
	{
		List<Person> allPerson = personRepository.findAll();
		Person randomPerson = allPerson.get(new Random().nextInt(allPerson.size()));
		logger.debug("getRandomPerson(): Random person fetched: {}", randomPerson);
		return randomPerson;
	}
}
