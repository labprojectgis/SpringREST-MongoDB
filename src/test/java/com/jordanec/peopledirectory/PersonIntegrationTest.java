package com.jordanec.peopledirectory;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.service.PersonService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK,classes=PeopleDirectoryApplication.class)
public class PersonIntegrationTest {
	@Autowired
	private PersonService personService;
	
	private final Logger logger = LoggerFactory.getLogger(PersonIntegrationTest.class);
	
	private MockMvc mockMvc;
	private MvcResult result;
	private ObjectMapper objectMapper;
	@Autowired
	private WebApplicationContext webApplicationContext;
	 
	@Before
	public void setup() throws Exception {
		logger.info("setup()");
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		objectMapper = new ObjectMapper();
		//testPersonCreate();
    }
	
    private void testPersonCreate() {
    	try {
    		logger.info("testPersonCreate()");
    		File persons_json = new ClassPathResource("data.json").getFile();
	    	//List<Person> persons = objectMapper.readValue(persons_json, objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
	    	BufferedInputStream bin = new BufferedInputStream(new FileInputStream(persons_json));
	    	byte[] buffer = new byte[(int) persons_json.length()];
	    	bin.read(buffer);
	    	String persons_json_string = new String(buffer);
	    	bin.close();

	    	logger.info("POST: data.json");
			logger.info("Path: /api/persons/");
			result = mockMvc.perform(post("/api/persons/")
					.contentType(MediaType.APPLICATION_JSON)
					.content(persons_json_string)
			    	).andReturn();
				
			logger.info("Status:"+result.getResponse().getStatus());
			assertThat(result.getResponse().getStatus(), is(HttpStatus.CREATED.value()));
		}  catch (Exception e) {
			e.printStackTrace();
		}	
    }
	
    @Test
    public void testPersonUpdate() {
    	try {
    		String id = "1";
    		logger.info("testPersonUpdate()");
	    	Person person = personService.getPerson(id);
    		assertNotNull(person);
    		person.setFirstName("Jordan");
    		person.setLastName("Espinoza");
	    	logger.info("PUT: "+person.toString());
			logger.info("Path: /api/persons/");
			result = mockMvc.perform(put("/api/persons/"+id)
					//.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsBytes(person))
			    	).andReturn();
				
			logger.info("Status:"+result.getResponse().getStatus());
			assertThat(result.getResponse().getStatus(), is(HttpStatus.NO_CONTENT.value()));
		}  catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
	@Test
	public void testPersonGetPerson() {
		try {
			String id = "5";
			logger.info("testPersonGetPerson()");
			logger.info("Path: /api/persons/"+id);
			result = mockMvc.perform(get("/api/persons/"+id)
					    .contentType(MediaType.APPLICATION_JSON)
					).andReturn();
			logger.info("Status:"+result.getResponse().getStatus());
			assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));
			logger.info("Content: "+result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPersonCount() {
		try {
			logger.info("testPersonCount()");
			logger.info("Path: /api/persons/count");
			result = mockMvc.perform(get("/api/persons/count")
					).andReturn();
			logger.info("Status:"+result.getResponse().getStatus());
			assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));
			Integer count = objectMapper.readValue(result.getResponse().getContentAsString(), Integer.class);
			logger.info("Content: "+count);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@After
	public void testPersonDelete() {
		try {
			logger.info("testPersonDelete()");
			for (int id=1;id<=1000;id++) {
				logger.info("Path: /api/persons/"+id);
				result = mockMvc.perform(delete("/api/persons/"+id)
					).andReturn();
			logger.info("Status:"+result.getResponse().getStatus());
			assertThat(result.getResponse().getStatus(), is(HttpStatus.NO_CONTENT.value()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
