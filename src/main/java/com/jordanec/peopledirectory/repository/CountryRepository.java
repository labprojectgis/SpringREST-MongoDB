package com.jordanec.peopledirectory.repository;

import com.jordanec.peopledirectory.model.Country;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CountryRepository extends MongoRepository<Country, String>, CountryRepositoryCustom
{
    Optional<Country> findByName(String name);

    Document createDocument(Document country);
}
