package com.jordanec.peopledirectory.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jordanec.peopledirectory.model.Person;
import org.springframework.data.mongodb.repository.Query;

public interface PersonRepository extends MongoRepository<Person, String>, PersonRepositoryCustom
{
	// http://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb.repositories.queries
//	Person findAndModify(Query query, Update update, FindAndModifyOptions options, Person entityClass);
	List<Person> findByFirstNameLike(String q);
	Stream<Person> readAllByDateOfBirthNotNullOrderByDateOfBirthDesc();
	List<Person> findByLastNameAndFirstNameAllIgnoreCase(String lastname, String firstname);
	List<Person> findByLastNameOrFirstNameAllIgnoreCase(String lastName, String firstName);
	List<Person> findByDateOfBirthBetweenOrderById(Date start, Date end);
	//@Query("{'mobile' : {'$gt' : ?0, '$lt' : ?1}}")	Query equivalent
	List<Person> findByMobileBetween(long start, long end);
	List<Person> findByGender(String gender);
	List<Person> findDistinctPeopleByCountryIgnoreCase(String country);
	Optional<Person> findByDni(Long dni);
	//
	@Aggregation(pipeline = {
			"{$lookup: {from: 'countries', localField: 'country._id', foreignField: '_id', as: 'country'}}",
			"{$unwind: {path: '$country' }}"})
	List<Person> lookupCountry();

/*	@TODO: define some aggregations
@Aggregation(" { $subtract: [ { $subtract:[{$year:'$$NOW'},{$year:'$dateOfBirth'}]},   {$cond:[ {$gt:[0, {$subtract:[{$dayOfYear:'$$NOW'}, "
			+ "   {$dayOfYear:'$dateOfBirth'}]}]}, 1, 0 ]}] } ")
	@Aggregation("{   "
			+ "		$project:  { "
			+ "			person.dni: 1.0, "
			+ "			person.firstName: 1.0, "
			+ "			person.lastName: 1.0 "
			+ "			dateDiff: { "
			+ "				$subtract: [new Date(), '$dateOfBirth'] "
			+ "			}  "

			+ "		}  "
			+ "}")
	List age();*/
}
