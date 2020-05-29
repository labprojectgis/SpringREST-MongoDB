package com.jordanec.peopledirectory.repository;

import com.jordanec.peopledirectory.model.Country;
import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

@RequiredArgsConstructor
public class CountryRepositoryImpl implements CountryRepositoryCustom
{
    private final static Logger logger = LoggerFactory.getLogger(CountryRepositoryImpl.class);
    private final MongoOperations mongoOperations;
    @Autowired
    PersonService personService;

    @Override
    public Document createDocument(Document country)
    {
        Document saved = mongoOperations.save(country, "countries");
        return saved;
    }

    @Override
    public Optional<Country> getCountryOfCurrentLocation(Long dni)
    {
        Optional<Person> optionalPerson = personService.findByDni(dni);
        if (!optionalPerson.isPresent() || optionalPerson.get().getCurrentLocation() == null)
        {
            logger.debug(
                    "getCountryOfCurrentLocation(): Person with dni: {} not found or doesn't have currentLocation assigned",
                    dni);
            return null;
        }
        return Optional.ofNullable(mongoOperations.findOne(new Query(new Criteria()
                        .orOperator(Criteria.where("geometryMulti").intersects(optionalPerson.get().getCurrentLocation()),
                                Criteria.where("geometry").intersects(optionalPerson.get().getCurrentLocation()))),
                Country.class));
    }
}
