package com.jordanec.peopledirectory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordanec.peopledirectory.model.Person;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestsUtil
{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_DATA_FOLDER = "data/";

    public static ParameterizedTypeReference<List<Person>> listPersonTypeReference()
    {
        return new ParameterizedTypeReference<List<Person>>(){};
    }
    public static ParameterizedTypeReference<Person> personTypeReference()
    {
        return new ParameterizedTypeReference<Person>(){};
    }

    public static <T> List<T> readList(String filename, Class<T> tClass) throws IOException
    {
        File file = new ClassPathResource(TEST_DATA_FOLDER + filename).getFile();
        return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
    }

    public static HttpHeaders createHeaders()
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
