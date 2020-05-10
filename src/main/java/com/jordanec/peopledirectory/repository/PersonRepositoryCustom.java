package com.jordanec.peopledirectory.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.jordanec.peopledirectory.model.Person;
import org.bson.Document;

public interface PersonRepositoryCustom
{
	List<Person> findBornBetween(LocalDate start, LocalDate end);
	long getCountByCountry(String country);
	List<Person> groupByCountry(String field, String order);
	Document groupDocumentByCountryOrdered(String field, String order);
	Person update(Person update);
	List<Person> lookupCountry(long dni);
	Person isOlderThan(long dni, int age);
	Document test();
}
