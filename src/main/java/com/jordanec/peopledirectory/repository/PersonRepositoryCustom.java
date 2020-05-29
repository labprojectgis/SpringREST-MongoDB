package com.jordanec.peopledirectory.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.jordanec.peopledirectory.model.Person;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;

public interface PersonRepositoryCustom
{
	List<Person> findBornBetween(LocalDate start, LocalDate end);
	Optional<Document> findDocumentByDni(Long dni);
	long getCountByCountry(String country);
	List<Person> groupByCountry(String field, String order);
	Document groupDocumentByCountryOrdered(String field, String order);
	List<Person> lookupCountry(long dni);
	Person isOlderThan(long dni, int age);
	List<Person> delete(List<Person> persons);
	UpdateResult addHobbies(Person person);
	UpdateResult pushHobbies(Person person);
	UpdateResult pullHobbies(Person person);
	UpdateResult addNewFieldsToAllHobbies(Document person);
	UpdateResult updateHobbiesGoodFrequency(Person person, Integer minFrequency);
	Document test();

	List<Person> findByCurrentLocationWithin(GeoJsonMultiPolygon multiPolygon);
}
