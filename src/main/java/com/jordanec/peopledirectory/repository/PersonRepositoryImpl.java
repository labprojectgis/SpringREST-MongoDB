package com.jordanec.peopledirectory.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import com.jordanec.peopledirectory.model.Person;

public class PersonRepositoryImpl implements PersonRepositoryCustom {
	@Autowired
    private MongoTemplate mongoTemplate;
	
	@Override
	public List<Person> findBornBetween(Date start, Date end) {
		Query query = new Query(Criteria.where("dateOfBirth").gt(start).lt(end));
		return mongoTemplate.find(query, Person.class);
		
	}

}
