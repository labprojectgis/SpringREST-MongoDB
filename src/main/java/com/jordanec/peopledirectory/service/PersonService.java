package com.jordanec.peopledirectory.service;

import com.jordanec.peopledirectory.model.Person;
import org.bson.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PersonService {
	Optional<Person> getById(String id);
	boolean exists(Person person);
	boolean existsById(String id);
	long count();
	List<Person> create(List<Person> persons);
	List<Person> save(List<Person> persons);
	Person create(Person person);
	Person save(Person person);
	void delete(String id);
	List<Person> findAll();
	Optional<Person> findByDni(Long dni);
	List<Person> findBornBetween(LocalDate start, LocalDate end);
	List<Person> findByDateOfBirthBetweenOrderById(Date start, Date end);
	List<Person> findByFirstNameLike(String q);
	List<Person> findByGender(String gender);
	List<Person> findByLastNameAndFirstNameAllIgnoreCase(String lastName, String firstName);
	List<Person> findByLastNameOrFirstNameAllIgnoreCase(String lastName, String firstName);
	//@Query("{'mobile' : {'$gt' : ?0, '$lt' : ?1}}")	Query equivalent
	List<Person> findByMobileBetween(long start, long end);
	List<Person> findDistinctPeopleByCountryIgnoreCase(String country);
	long getCountByCountry(String country);
	List<Person> groupByCountry(String field, String order);
	Document groupDocumentByCountryOrdered(String field, String order);
	List<Person> readAllByDateOfBirthNotNullOrderByDateOfBirthDesc();
	List<Person> lookupCountry(Long dni);
	Person isOlderThan(long dni, int age);

	Document test();
}
