package com.jordanec.peopledirectory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import org.springframework.data.domain.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.repository.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	PersonRepository personRepository;
	
	private final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

	@Override
	public Person getPerson(String id) {
		logger.info("getPerson("+id+")");
        return personRepository.findOne(id);
	}

	@Override
	public Boolean personExists(Person person) {
		logger.info("getPerson("+person+")");
		return personRepository.exists(Example.of(person));
	}
	
	@Override
	public Boolean personExists(String id) {
		logger.info("getPerson("+id+")");
		return personRepository.exists(id);
	}
	
	@Override
	public Integer countPersons() {
		logger.info("getPerson()");
		return personRepository.countAllByIdNotNull();
	}

	@Override
	public List<Person> getPersons() {
		logger.info("getPerson()");
        return personRepository.findAll();
	}

	@Override
	public List<Person> findBornBetween(Date start, Date end) {
		logger.info("getPerson("+start+", "+end+")");
		return personRepository.findBornBetween(start, end);
	}

	
	@Override
	public List<Person> createPersons(List<Person> persons) {
		logger.info("getPerson(persons(size:"+persons.size()+"))");
		return personRepository.save(persons);
	}

	@Override
	public Person createPerson(Person person) {
		logger.info("getPerson("+person+")");
		return personRepository.save(person);
	}
	
	@Override
	public List<Person> findByFirstNameLike(String q) {
		logger.info("getPerson("+q+")");
		return personRepository.findByFirstNameLike(q);
	}

	@Override
	public List<Person> readAllByIdNotNullOrderByIdDesc() {
		logger.info("getPerson()");
		return personRepository.readAllByIdNotNullOrderByIdDesc();
	}

	@Override
	public List<Person> findByLastNameAndFirstNameAllIgnoreCase(String lastname, String firstname) {
		logger.info("getPerson("+lastname+","+firstname+")");
		return personRepository.findByLastNameAndFirstNameAllIgnoreCase(lastname, firstname);
	}

	@Override
	public List<Person> findByLastNameOrFirstNameAllIgnoreCase(String lastname, String firstname) {
		logger.info("getPerson("+lastname+","+firstname+")");
		return personRepository.findByLastNameOrFirstNameAllIgnoreCase(lastname, firstname);
	}

	@Override
	public Integer countAllByIdNotNull() {
		logger.info("getPerson()");
		return personRepository.countAllByIdNotNull();
	}

	@Override
	public List<Person> findByDateOfBirthBetweenOrderById(Date start, Date end) {
		logger.info("getPerson("+start+", "+end+")");
		return personRepository.findByDateOfBirthBetweenOrderById(start, end);
	}

	@Override
	public List<Person> findByMobileBetween(long start, long end) {
		logger.info("getPerson("+start+", "+end+")");
		return personRepository.findByMobileBetween(start, end);
	}

	@Override
	public List<Person> findByGender(String gender) {
		logger.info("getPerson("+gender+")");
		return personRepository.findByGender(gender);
	}

	@Override
	public List<Person> findDistinctPeopleByCountryIgnoreCase(String country) {
		logger.info("getPerson("+country+")");
		return personRepository.findDistinctPeopleByCountryIgnoreCase(country);
	}

}
