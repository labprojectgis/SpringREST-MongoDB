package com.jordanec.peopledirectory.service;

import com.jordanec.peopledirectory.model.Country;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

public interface CountryService
{
//    Optional<Country> getById(String id);
//    boolean exists(Country country);
//    boolean existsById(String id);
//    long count();
    List<Country> create(List<Country> countries);
    List<Country> save(List<Country> countries);
    Country create(Country country);
    Country save(Country country);
//    void delete(String id);
    List<Country> findAll();
    Optional<Country> findByName(String name);

    Document createDocument(Document country);

    Optional<Country> getCountryOfCurrentLocation(Long dni);
}
