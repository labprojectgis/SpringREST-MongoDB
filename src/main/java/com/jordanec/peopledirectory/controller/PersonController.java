package com.jordanec.peopledirectory.controller;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.repository.PersonRepository;

@RepositoryRestController
public class PersonController {
	
    private final PersonRepository repository;
    
	private final Logger logger = LoggerFactory.getLogger(PersonController.class);
    
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    @Autowired
    public PersonController(PersonRepository personRepository) { 
    	repository = personRepository;
    }
	   
    
    /*
     * http://localhost:8080/api/persons/5
     * 
     * */
    @RequestMapping(method = RequestMethod.GET, value = "persons/{id}") 
    public @ResponseBody ResponseEntity<?> getPerson(@PathVariable String id) {
        Person person = repository.findOne(id);
        if (person == null) {
        	logger.error("Person with id="+id+" not found");
        	return new ResponseEntity<Resources<Person>>(HttpStatus.NOT_FOUND);
        }
        logger.info("Person with id="+id+" found");
        Resource<Person> resource = new Resource<Person>(person); 
        resource.add(linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel());
        resource.add(linkTo(methodOn(PersonController.class).getPerson(id)).withRel("person"));
        return ResponseEntity.ok(resource); 
    }
    
    /*@RequestMapping(method = RequestMethod.GET, value = "persons") 
    public @ResponseBody ResponseEntity<?> getPersons() {
        List<Person> persons = repository.findAll();
        Resources<Person> resources = new Resources<Person>(persons);
        
        for (Person p : persons) {
    		resources.add(linkTo(methodOn(PersonController.class).getPerson(p.getId())).withSelfRel());
		}
        resources.add(linkTo(methodOn(PersonController.class).getPersons()).withSelfRel());
        logger.info("getPersons() called");
        return ResponseEntity.ok(resources); 
    }*/
    
    /**
     * 
     * @return Total of Person registers
     */
    @RequestMapping(method = RequestMethod.GET, value="persons/count")
    public @ResponseBody ResponseEntity<?> countPersons(){
    	return ResponseEntity.ok(repository.countAllByIdNotNull());
    }
    
    
    /*
     * 	http://localhost:8080/api/persons/findBornBetween?start=01/01/1980&end=12/31/1980
     * */
    @RequestMapping(method = RequestMethod.GET, params= {"start","end"}, value="persons/findBornBetween")
    public @ResponseBody ResponseEntity<?> findBornBetween(@RequestParam("start") String start, @RequestParam("end") String end){
    	logger.info("findBornBetween() request! Params: start= "+start+" end="+end);
    	try {
    		Date s = dateFormat.parse(start);
    		Date e = dateFormat.parse(end);
	    	logger.info("Params formated: start= "+s+" end="+e);
	    	List<Person> persons = repository.findBornBetween(s, e);
	    	return ResponseEntity.ok(persons);
    	} catch (ParseException e1) {
    		logger.error(e1.getMessage());
			return new ResponseEntity<Resources<Person>>(HttpStatus.BAD_REQUEST);
		}
    }
    
    
    /*
     * to POST a list of persons
     * */
    @RequestMapping(value = "persons",method = RequestMethod.POST) 
    public ResponseEntity<?> createPersons(@RequestBody List<Person> persons){
    	Resources<Person> resources = new Resources<Person>(persons);
    	resources.add(linkTo(methodOn(PersonController.class).createPersons(null)).withSelfRel());
    	
    	for (Person person : persons) {
			if (!repository.exists(Example.of(person))){
				Person person_saved = repository.save(person);
				if (person_saved != null) {
					resources.add(linkTo(methodOn(PersonController.class).getPerson(person_saved.getId())).withSelfRel());
					resources.add(linkTo(methodOn(PersonController.class).getPerson(person_saved.getId())).withRel("person"));
				}
				else
					logger.error("Error saving Person: "+person.getFirstName());
			}
			else {
				resources.add(linkTo(methodOn(PersonController.class).getPerson(person.getId())).withSelfRel());
				logger.error("This person already exists: "+person.getFirstName());
			}
		}
    	
    	URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/persons").build().toUri();
    	HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<Resources<Person>>(resources,headers, HttpStatus.CREATED);
    }
       
}