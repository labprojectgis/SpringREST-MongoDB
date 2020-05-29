package com.jordanec.peopledirectory.controller;

import com.jordanec.peopledirectory.PeopleDirectoryApplication;
import com.jordanec.peopledirectory.TestsUtil;
import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.repository.PersonRepository;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= PeopleDirectoryApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PersonControllerTest
{
    @LocalServerPort
    private int port;

    TestRestTemplate testRestTemplate = new TestRestTemplate();
    List<Person> testPersonList;

    @Autowired
    PersonRepository personRepository;

    @After
    public void tearDown()
    {
        if (!CollectionUtils.isEmpty(testPersonList))
        {
            Assert.assertEquals(testPersonList.size(), personRepository.delete(testPersonList).size());
            testPersonList = null;
        }
    }

    @Test
    public void findAll()
    {
        ResponseEntity<List<Person>> responseEntity = testRestTemplate
                .exchange(buildURL() + "person", HttpMethod.GET, new HttpEntity<>(TestsUtil.createHeaders()),
                        TestsUtil.listPersonTypeReference());
        Assert.assertThat(responseEntity.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
        List<Person> responseList = responseEntity.getBody();
        Assert.assertNotNull(responseList);
        Assert.assertEquals(1000, responseList.size());
    }

    @Test
    public void findByDni_OK()
    {
        Long dni = 234534568L;
        ResponseEntity<Person> responseEntity = testRestTemplate
                .exchange(buildURL() + "person/findByDni/{dni}", HttpMethod.GET,
                        new HttpEntity<>(TestsUtil.createHeaders()), TestsUtil.personTypeReference(), dni);
        Assert.assertThat(responseEntity.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
        Person responsePerson = responseEntity.getBody();
        Assert.assertNotNull(responsePerson);
        Assert.assertThat(dni, CoreMatchers.equalTo(responsePerson.getDni()));
        Assert.assertEquals(-69.968593, responsePerson.getCurrentLocation().getX(), 0);
        Assert.assertEquals(12.508378, responsePerson.getCurrentLocation().getY(), 0);
    }

    @Test
    public void findByDni_NotFound()
    {
        Long dni = 123456L;
        ResponseEntity<Person> responseEntity = testRestTemplate
                .exchange(buildURL() + "person/findByDni/{dni}", HttpMethod.GET,
                        new HttpEntity<>(TestsUtil.createHeaders()), TestsUtil.personTypeReference(), dni);
        Assert.assertThat(responseEntity.getStatusCode(), CoreMatchers.equalTo(HttpStatus.NOT_FOUND));
        Assert.assertNull(responseEntity.getBody());
    }

    @Test
    public void bulkInsert_OK() throws IOException
    {
        List<Person> persons = TestsUtil.readList("Person_1.json", Person.class);
        ResponseEntity<List<Person>> responseEntity = testRestTemplate
                .exchange(buildURL() + "person/bulkInsert", HttpMethod.POST,
                        new HttpEntity<>(persons, TestsUtil.createHeaders()), TestsUtil.listPersonTypeReference());
        Assert.assertThat(responseEntity.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
        List<Person> responseList = responseEntity.getBody();
        Assert.assertNotNull(responseList);
        testPersonList = responseList;
        Assert.assertEquals(persons.size(), responseList.size());
    }

    @Test
    public void insert_OK() throws IOException
    {
        List<Person> persons = TestsUtil.readList("Person_1.json", Person.class);
        ResponseEntity<Person> responseEntity = testRestTemplate
                .exchange(buildURL() + "person", HttpMethod.POST,
                        new HttpEntity<>(persons.get(0), TestsUtil.createHeaders()), TestsUtil.personTypeReference());
        Assert.assertThat(responseEntity.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
        Person responsePerson = responseEntity.getBody();
        testPersonList = Collections.singletonList(responsePerson);
        Assert.assertNotNull(responsePerson);
    }

    @Test
    public void bulkDelete_OK() throws IOException
    {
        bulkInsert_OK();
        ResponseEntity<List<Person>> responseEntity = testRestTemplate
                .exchange(buildURL() + "person/bulkDelete", HttpMethod.DELETE,
                        new HttpEntity<>(testPersonList, TestsUtil.createHeaders()), TestsUtil.listPersonTypeReference());
        Assert.assertThat(responseEntity.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
        List<Person> responseList = responseEntity.getBody();
        Assert.assertNotNull(responseList);
        Assert.assertEquals(testPersonList.size(), responseList.size());
        Assert.assertThat(personRepository.count(), CoreMatchers.equalTo(1000L));
        testPersonList.clear();
    }

/*
    //TODO: adjust below tests
    @Test
    public void testPersonUpdate() {
        try {
            String id = "1";
            Optional<Person> optionalPerson = personService.getById(id);
            assertTrue(optionalPerson.isPresent());
            optionalPerson.get().setFirstName("Jordan");
            optionalPerson.get().setLastName("Espinoza");
            result = mockMvc.perform(put("/api/persons/"+id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(optionalPerson.get()))
            ).andReturn();

            assertThat(result.getResponse().getStatus(), is(HttpStatus.NO_CONTENT.value()));
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPersonGetPerson() {
        try {
            String id = "5";
            result = mockMvc.perform(get("/api/persons/"+id)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andReturn();
            assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPersonCount() {
        result = mockMvc.perform(get("/api/persons/count")
        ).andReturn();
        assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));
        Integer count = objectMapper.readValue(result.getResponse().getContentAsString(), Integer.class);

    }

    @Test
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
    }*/

    private String buildURL()
    {
        return "http://localhost:" + port + "/people/api/";
    }
}
