package com.jordanec.peopledirectory.repository;

import java.util.Date;
import java.util.List;

import com.jordanec.peopledirectory.model.Person;

public interface PersonRepositoryCustom {
	public List<Person> findBornBetween(Date start, Date end);
	
}
