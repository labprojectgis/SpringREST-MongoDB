package com.jordanec.peopledirectory.repository;

import com.jordanec.peopledirectory.model.Country;
import org.bson.Document;

import java.util.Optional;

public interface CountryRepositoryCustom
{
    Document createDocument(Document country);
    Optional<Country> getCountryOfCurrentLocation(Long dni);
}
