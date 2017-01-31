package com.jordanec.peopledirectory.service;

import java.util.Date;
import java.util.List;

import com.jordanec.peopledirectory.model.Person;

public interface PersonService {
	Person getPerson(String id);
	
	Boolean personExists(Person person);
	Boolean personExists(String id);
	
	Integer countPersons();
	List<Person> getPersons();
	List<Person> findBornBetween(Date start, Date end);
	List<Person> createPersons(List<Person> persons);
	Person createPerson(Person person);
	List<Person> findByFirstNameLike(String q);
	
	List<Person> readAllByIdNotNullOrderByIdDesc();
	
	/*
	 *	http://localhost:8080/api/persons/search/findByLastNameAndFirstNameAllIgnoreCase?l=jenkins&f=Katherine	= [1]
	 *	http://localhost:8080/api/persons/search/findByLastNameAndFirstNameAllIgnoreCase?l=Bishop&f=Katherine	= []
	 *	http://localhost:8080/api/persons/search/findByLastNameAndFirstNameAllIgnoreCase?l=Bishop&f				= []
	 **/
	List<Person> findByLastNameAndFirstNameAllIgnoreCase(String lastname, String firstname);
	
	/*
	 *	http://localhost:8080/api/persons/search/findByLastNameOrFirstNameAllIgnoreCase?l=Bishop&f=Katherine
	 *	http://localhost:8080/api/persons/search/findByLastNameOrFirstNameAllIgnoreCase?l=jenkins&f=
	 */
	List<Person> findByLastNameOrFirstNameAllIgnoreCase(String lastname, String firstname);

	/*
	 *	http://localhost:8080/api/persons/search/countAllByIdNotNull 
	 **/
	Integer countAllByIdNotNull();
	
	/*
	 *	http://localhost:8080/api/persons/search/findByDateOfBirthBetweenOrderById?start=1980/01/01&end=1980/12/31
	 * */
	List<Person> findByDateOfBirthBetweenOrderById(Date start, Date end);
	
	
	/*
	 *	http://localhost:8080/api/persons/search/findByMobileBetween?start=85000000&end=85300000
	 * */
	//@Query("{'mobile' : {'$gt' : ?0, '$lt' : ?1}}")	Query equivalent
	List<Person> findByMobileBetween(long start, long end);
	
	
	/*
	 * http://localhost:8080/api/persons/search/findByGender?gender=Male
	 * */
	List<Person> findByGender(String gender);
	
	/*
	 * 	http://localhost:8080/api/persons/search/findDistinctPeopleByCountryIgnoreCase?country=portugal
	 * */
	List<Person> findDistinctPeopleByCountryIgnoreCase(String country); 
	
}
