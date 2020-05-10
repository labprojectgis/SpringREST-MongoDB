package com.jordanec.peopledirectory.service;

import com.jordanec.peopledirectory.model.Country;
import com.jordanec.peopledirectory.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService
{
    @Autowired
    CountryRepository countryRepository;

    @Override
    public List<Country> create(List<Country> countries)
    {
        return countryRepository.insert(countries);
    }

    @Override
    public List<Country> save(List<Country> countries)
    {
        return countryRepository.saveAll(countries);
    }

    @Override
    public Country create(Country country)
    {
        return countryRepository.insert(country);
    }

    @Override
    public Country save(Country country)
    {
        Optional<Country> countryOptional = countryRepository.findByName(country.getName());
        countryOptional.ifPresent(value -> country.setId(value.getId()));
        return countryRepository.save(country);
    }

    @Override
    public List<Country> findAll()
    {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findByName(String name)
    {
        return countryRepository.findByName(name);
    }
}
