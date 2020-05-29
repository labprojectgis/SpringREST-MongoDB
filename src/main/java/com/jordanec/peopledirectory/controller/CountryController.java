package com.jordanec.peopledirectory.controller;

import com.jordanec.peopledirectory.model.Country;
import com.jordanec.peopledirectory.service.CountryService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CountryController
{
    @Autowired
    CountryService countryService;
    
    /**
     * WRITE APIs
     */

    @RequestMapping(value = "/country/bulkCreate",method = RequestMethod.POST)
    public ResponseEntity<List<Country>> bulkCreate(@RequestBody List<Country> countries) {
        return new ResponseEntity<>(countryService.create(countries), HttpStatus.OK);
    }

    @RequestMapping(value = "/country/bulkSave",method = RequestMethod.POST)
    public ResponseEntity<List<Country>> bulkSave(@RequestBody List<Country> countries) {
        return new ResponseEntity<>(countryService.save(countries), HttpStatus.OK);
    }

    @RequestMapping(value = "/country",method = RequestMethod.POST)
    public ResponseEntity<Country> create(@RequestBody Country country) {
        return new ResponseEntity<>(countryService.create(country), HttpStatus.OK);
    }
    @RequestMapping(value = "/countryDocument",method = RequestMethod.POST)
    public ResponseEntity<Document> createDocument(@RequestBody Document country) {
        return new ResponseEntity<>(countryService.createDocument(country), HttpStatus.OK);
    }

    @RequestMapping(value = "/country/save",method = RequestMethod.POST)
    public ResponseEntity<Country> bulkSave(@RequestBody Country country) {
        return new ResponseEntity<>(countryService.save(country), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/country")
    public @ResponseBody ResponseEntity<List<Country>> findAll() {
        List<Country> countries = countryService.findAll();
        return ResponseEntity.ok(countries);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/country/findByName")
    public @ResponseBody ResponseEntity<Country> findByName(@RequestParam(value = "name") String name)
    {
        return countryService.findByName(name).map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/country/getCountryOfCurrentLocation")
    public @ResponseBody ResponseEntity<Country> getCountryOfCurrentLocation(@RequestParam(value = "dni") Long dni)
    {
        Optional<Country> optionalCountry = countryService.getCountryOfCurrentLocation(dni);
        return optionalCountry.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
