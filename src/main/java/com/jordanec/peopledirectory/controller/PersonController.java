package com.jordanec.peopledirectory.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.service.PersonService;

@RestController
@RequestMapping("/api")
public class PersonController {
	
	@Autowired
	PersonService personService;
	
	private final Logger logger = LoggerFactory.getLogger(PersonController.class);
    
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * WRITE APIs
     */

    @RequestMapping(value = "/person/bulkInsert",method = RequestMethod.POST)
    public ResponseEntity<List<Person>> bulkCreate(@RequestBody List<Person> persons) {
        return new ResponseEntity<>(personService.insert(persons), HttpStatus.OK);
    }

    @RequestMapping(value = "/person/bulkSave",method = RequestMethod.POST)
    public ResponseEntity<List<Person>> bulkSave(@RequestBody List<Person> persons) {
        return new ResponseEntity<>(personService.save(persons), HttpStatus.OK);
    }

    @RequestMapping(value = "/person",method = RequestMethod.POST)
    public ResponseEntity<Person> bulkCreate(@RequestBody Person person) {
        return new ResponseEntity<>(personService.insert(person), HttpStatus.OK);
    }

    @RequestMapping(value = "/person/save",method = RequestMethod.POST)
    public ResponseEntity<Person> bulkSave(@RequestBody Person person) {
        return new ResponseEntity<>(personService.save(person), HttpStatus.OK);
    }

    @RequestMapping(value = "/person/delete/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        personService.delete(id);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/person/bulkDelete",method = RequestMethod.DELETE)
    public ResponseEntity<List<Person>> bulkDelete(@RequestBody List<Person> persons) {
        return new ResponseEntity<>(personService.delete(persons), HttpStatus.OK);
    }

    /**
     * READ APIs
     */

    @RequestMapping(method = RequestMethod.GET, value = "/person/{id}")
    public @ResponseBody ResponseEntity<Person> getPerson(@PathVariable String id) {
        Optional<Person> optionalPerson = personService.getById(id);
        return optionalPerson.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.GET, value="/person/count")
    public @ResponseBody ResponseEntity<?> count()
    {
        return ResponseEntity.ok(personService.count());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/person")
    public @ResponseBody ResponseEntity<List<Person>> findAll() {
        List<Person> persons = personService.findAll();
        return ResponseEntity.ok(persons);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/person/findByDni/{dni}")
    public @ResponseBody ResponseEntity<Person> findByDni(@PathVariable Long dni)
    {
        Optional<Person> optionalPerson = personService.findByDni(dni);
//        return ResponseEntity.ok(personOptional);
        return optionalPerson.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * FIND APIs
     */

    /*
     * 	/api/person/findBornBetween?start=01/01/1980&end=01/02/1980
     * */
    @RequestMapping(method = RequestMethod.GET, value="/person/findBornBetween")
    public @ResponseBody ResponseEntity<List<Person>> findBornBetween(@RequestParam("start")
    @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
            @RequestParam("end") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end)
    {
        try
        {
    	    logger.debug("findBornBetween(start, end): start={} end={}", start, end);
            List<Person> persons = personService.findBornBetween(start, end);
            return ResponseEntity.ok(persons);
        }
        catch (Exception ex)
        {
            logger.error("findBornBetween()", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
        /api/person/findByFirstNameLike?firstName=aso
     */
    @RequestMapping(value="/person/findByDateOfBirthBetweenOrderById",params= "firstName",method=RequestMethod.GET)
    public ResponseEntity<List<Person>> findByDateOfBirthBetweenOrderById(@RequestParam("start") String start,
            @RequestParam("end") String end){
        try
        {
            logger.debug("findByDateOfBirthBetweenOrderById(start, end): start={} end={}", start, end);
            Date startParsed = dateFormat.parse(start);
            Date endParsed = dateFormat.parse(end);
            List<Person> persons = personService.findByDateOfBirthBetweenOrderById(startParsed, endParsed);
            return new ResponseEntity<>(persons, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            logger.error("findByDateOfBirthBetweenOrderById()", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /*
        /api/person/findDistinctPeopleByCountry?country=Costa%20Rica

     */
    @RequestMapping(value = "/person/findDistinctPeopleByCountry", method = RequestMethod.GET)
    public ResponseEntity<List<Person>> findDistinctPeopleByCountry(@RequestParam("country") String country)
    {
        List<Person> persons = personService.findDistinctPeopleByCountryIgnoreCase(country);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    ///api/person/findByFirstNameLike?firstName=the
    @RequestMapping(value="/person/findByFirstNameLike", method=RequestMethod.GET)
    public ResponseEntity<List<Person>> findByFirstNameLike(@RequestParam("firstName") String firstName){
        List<Person> persons = personService.findByFirstNameLike(firstName);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    ///api/person/findByGender?gender=Female
    @RequestMapping(value="/person/findByGender", method=RequestMethod.GET)
    public ResponseEntity<List<Person>> findByGender(@RequestParam("gender") String gender){
        List<Person> persons = personService.findByGender(gender);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    ///api/person/findByLastNameAndFirstName?lastName=Jenkins&firstName=Katherine
    @RequestMapping(value="/person/findByLastNameAndFirstName", method=RequestMethod.GET)
    public ResponseEntity<List<Person>> findByLastNameAndFirstName(@RequestParam("lastName") String lastName,
            @RequestParam("firstName") String firstName)
    {
        List<Person> persons = personService.findByLastNameAndFirstNameAllIgnoreCase(lastName, firstName);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    ///api/person/findByLastNameOrFirstName?lastName=Jenkins&firstName=Katherine
    @RequestMapping(value="/person/findByLastNameOrFirstName", method=RequestMethod.GET)
    public ResponseEntity<List<Person>> findByLastNameOrFirstName(@RequestParam("lastName") String lastName,
            @RequestParam("firstName") String firstName) {
        List<Person> persons = personService.findByLastNameOrFirstNameAllIgnoreCase(lastName, firstName);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    ///api/person/findByMobileBetween?start=96414376&end=96436478
    @RequestMapping(value="/person/findByMobileBetween", method=RequestMethod.GET)
    public ResponseEntity<List<Person>> findByMobileBetween(@RequestParam("start") long start,
            @RequestParam("end") long end) {
        List<Person> persons = personService.findByMobileBetween(start, end);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // api/person/getCountByCountry?country=China
    @RequestMapping(value="/person/getCountByCountry", method=RequestMethod.GET)
    public ResponseEntity<Long> getCountByCountry(@RequestParam("country") String country) {
        return ResponseEntity.ok().body(personService.getCountByCountry(country));
    }
    // api/person/groupByCountry
    @RequestMapping(value = "/person/groupByCountry", method = RequestMethod.GET)
    public ResponseEntity<List<Person>> groupByCountry(@RequestParam(value = "field", required = false)
            String field, @RequestParam(value = "order", required = false) String order)
    {
        return ResponseEntity.ok(personService.groupByCountry(field, order));
    }
    // api/person/groupDocumentByCountryOrdered?field=total&order=DESC
    @RequestMapping(value = "/person/groupDocumentByCountryOrdered", method = RequestMethod.GET)
    public ResponseEntity<Document> groupDocumentByCountryOrdered(@RequestParam(value = "field", required = false)
            String field, @RequestParam(value = "order", required = false) String order)
    {
        return ResponseEntity.ok(personService.groupDocumentByCountryOrdered(field, order));
    }

    @RequestMapping(value = "/person/readAllAges", method = RequestMethod.GET)
    public ResponseEntity<List<Person>> readAllAges()
    {
        return ResponseEntity.ok(personService.readAllByDateOfBirthNotNullOrderByDateOfBirthDesc());
    }
    // api/person/lookupCountry?dni=290978673
    // api/person/lookupCountry
    @RequestMapping(value = "/person/lookupCountry", method = RequestMethod.GET)
    public ResponseEntity<List<Person>> lookupCountry(@RequestParam(value = "dni", required = false) Long dni)
    {
        return ResponseEntity.ok(personService.lookupCountry(dni));
    }

    // api/person/isOlderThan/dni/290978673/age/40
    @RequestMapping(value = "/person/isOlderThan/dni/{dni}/age/{age}", method = RequestMethod.GET)
    public ResponseEntity<Person> isOlderThan(@PathVariable(value = "dni") Long dni, @PathVariable(value = "age") Integer age)
    {
        return ResponseEntity.ok(personService.isOlderThan(dni, age));
    }

    //Test
    @RequestMapping(value = "/person/test", method = RequestMethod.GET)
    public ResponseEntity<Document> test()
    {
        return ResponseEntity.ok(personService.test());
    }
}