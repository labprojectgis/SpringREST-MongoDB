package com.jordanec.peopledirectory.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import com.jordanec.peopledirectory.model.Person;

public interface PersonRepository extends MongoRepository<Person, String>, PersonRepositoryCustom {
	// http://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb.repositories.queries
	
	
	/*
	 * http://localhost:8080/api/persons/search/findByfirstNameLike?q=a
	 **/
	public List<Person> findByFirstNameLike(@Param("q") String q);
	
	public List<Person> readAllByIdNotNullOrderByIdDesc();
	
	/*
	 *	http://localhost:8080/api/persons/search/findByLastNameAndFirstNameAllIgnoreCase?l=jenkins&f=Katherine	= [1]
	 *	http://localhost:8080/api/persons/search/findByLastNameAndFirstNameAllIgnoreCase?l=Bishop&f=Katherine	= []
	 *	http://localhost:8080/api/persons/search/findByLastNameAndFirstNameAllIgnoreCase?l=Bishop&f				= []
	 **/
	public List<Person> findByLastNameAndFirstNameAllIgnoreCase(@Param("l") String lastname, @Param("f") String firstname);
	
	/*
	 *	http://localhost:8080/api/persons/search/findByLastNameOrFirstNameAllIgnoreCase?l=Bishop&f=Katherine
	 *	http://localhost:8080/api/persons/search/findByLastNameOrFirstNameAllIgnoreCase?l=jenkins&f=
	 */
	public List<Person> findByLastNameOrFirstNameAllIgnoreCase(@Param("l") String lastname, @Param("f") String firstname);

	/*
	 *	http://localhost:8080/api/persons/search/countAllByIdNotNull 
	 **/
	public Integer countAllByIdNotNull();
	
	/*
	 *	http://localhost:8080/api/persons/search/findByDateOfBirthBetweenOrderById?start=1980/01/01&end=1980/12/31
	 * */
	public List<Person> findByDateOfBirthBetweenOrderById(@Param("start") Date start, @Param("end") Date end);
	
	
	/*
	 *	http://localhost:8080/api/persons/search/findByMobileBetween?start=85000000&end=85300000
	 * */
	//@Query("{'mobile' : {'$gt' : ?0, '$lt' : ?1}}")	Query equivalent
	public List<Person> findByMobileBetween(@Param("start") long start, @Param("end") long end);
	
	
	/*
	 * http://localhost:8080/api/persons/search/findByGender?gender=Male
	 * */
	public List<Person> findByGender(@Param("gender") String gender);
	
	/*
	 * 	http://localhost:8080/api/persons/search/findDistinctPeopleByCountryIgnoreCase?country=portugal
	 * */
	public List<Person> findDistinctPeopleByCountryIgnoreCase(@Param("country") String country); 
	
}
