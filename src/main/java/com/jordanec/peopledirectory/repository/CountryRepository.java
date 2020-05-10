package com.jordanec.peopledirectory.repository;

import com.jordanec.peopledirectory.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CountryRepository extends MongoRepository<Country, String>
{
    Optional<Country> findByName(String name);
}
