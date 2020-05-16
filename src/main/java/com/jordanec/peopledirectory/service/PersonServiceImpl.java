package com.jordanec.peopledirectory.service;

import com.jordanec.peopledirectory.model.Country;
import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.repository.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	CountryService countryService;

	@Override
	public Optional<Person> getById(String id) {
        return personRepository.findById(id);
	}

	@Override
	public boolean exists(Person person) {
		return personRepository.exists(Example.of(person));
	}
	
	@Override
	public boolean existsById(String id) {
		return personRepository.existsById(id);
	}
	
	@Override
	public long count() {
		return personRepository.count();
	}

	@Override
	public List<Person> findAll() {
        return personRepository.findAll();
	}
	@Override
	public Optional<Person> findByDni(Long dni)
	{
		return personRepository.findByDni(dni);
	}

	@Override
	public List<Person> findBornBetween(LocalDate start, LocalDate end)
	{
		return personRepository.findBornBetween(start, end);
	}

	@Override
	public List<Person> create(List<Person> persons) {
		persons.forEach(this::assignCountryId);
		return personRepository.insert(persons);
	}
	@Override
	public List<Person> save(List<Person> persons) {
		persons.forEach(this::assignCountryId);
		return personRepository.saveAll(persons);
	}

	@Override
	public Person create(Person person) {
		assignCountryId(person);
		return personRepository.insert(person);
	}

	@Override
	public Person save(Person person)
	{
		assignCountryId(person);
		Optional<Person> personOptional = personRepository.findByDni(person.getDni());
		personOptional.ifPresent(p -> person.setId(p.getId()));
		return personRepository.save(person);
	}

	@Override
	public void delete(String id)
	{
		personRepository.deleteById(id);
	}

	@Override
	public List<Person> findByFirstNameLike(String q) {
		return personRepository.findByFirstNameLike(q);
	}

	@Override
	public List<Person> readAllByDateOfBirthNotNullOrderByDateOfBirthDesc() {
		List<Person> personList = personRepository.readAllByDateOfBirthNotNullOrderByDateOfBirthDesc().collect(
				Collectors.toList());
		personList.parallelStream().forEach(p -> p.setAge(Period.between(p.getDateOfBirth(), LocalDate.now()).getYears()));
		return personList;
	}
	@Override
	public List<Person> lookupCountry(Long dni) {
		List<Person> personList;
		if (dni == null)
		{
			personList = personRepository.lookupCountry();
		}
		else
		{
			personList = personRepository.lookupCountry(dni);
		}
		return personList;
	}

	@Override
	public List<Person> findByLastNameAndFirstNameAllIgnoreCase(String lastName, String firstName) {
		return personRepository.findByLastNameAndFirstNameAllIgnoreCase(lastName, firstName);
	}

	@Override
	public List<Person> findByLastNameOrFirstNameAllIgnoreCase(String lastName, String firstName) {
		return personRepository.findByLastNameOrFirstNameAllIgnoreCase(lastName, firstName);
	}

	@Override
	public List<Person> findByDateOfBirthBetweenOrderById(Date start, Date end) {
		return personRepository.findByDateOfBirthBetweenOrderById(start, end);
	}

	@Override
	public List<Person> findByMobileBetween(long start, long end) {
		return personRepository.findByMobileBetween(start, end);
	}

	@Override
	public List<Person> findByGender(String gender) {
		return personRepository.findByGender(gender);
	}

	@Override
	public List<Person> findDistinctPeopleByCountryIgnoreCase(String country) {
		return personRepository.findDistinctPeopleByCountryIgnoreCase(country);
	}

	@Override
	public long getCountByCountry(String country)
	{
		return personRepository.getCountByCountry(country);
	}

	@Override
    public List<Person> groupByCountry(String field, String order)
	{
        return personRepository.groupByCountry(field, order);
	}
	@Override
    public Document groupDocumentByCountryOrdered(String field, String order)
	{
        return personRepository.groupDocumentByCountryOrdered(field, order);
	}
	@Override
	public Person isOlderThan(long dni, int age)
	{
		return personRepository.isOlderThan(dni, age);
	}

	//test
	@Override
	public Document test()
	{
		return personRepository.test();
	}

	private void assignCountryId(Person person)
	{
		if (person.getCountry() != null && StringUtils.isNotBlank(person.getCountry().getName()))
		{
			Optional<Country> countryOptional = countryService.findByName(person.getCountry().getName());
			countryOptional.ifPresent(country -> {
				person.getCountry().setId(country.getId());
				person.getCountry().setName(null);
			});
		}
	}
}
