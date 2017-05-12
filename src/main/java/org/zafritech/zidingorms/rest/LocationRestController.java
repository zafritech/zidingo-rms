/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.zafritech.zidingorms.database.domain.City;
import org.zafritech.zidingorms.database.domain.Country;
import org.zafritech.zidingorms.database.domain.State;
import org.zafritech.zidingorms.database.repositories.CityRepository;
import org.zafritech.zidingorms.database.repositories.CountryRepository;
import org.zafritech.zidingorms.database.repositories.StateRepository;

/**
 *
 * @author LukeS
 */
@Controller
public class LocationRestController {
    
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private StateRepository stateRepository;
    
    @Autowired
    private CityRepository cityRepository;
   
    @RequestMapping(value = "/api/countries/list", method = GET)
    public ResponseEntity<List<Country>> getCountriesList() {
        
        List<Country> countries = countryRepository.findAll();
        
        return new ResponseEntity<List<Country>>(countries, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/states/list/{id}", method = GET)
    public ResponseEntity<List<State>> getStatesList(@PathVariable(value = "id") Long id) {
        
        List<State> states = stateRepository.findByCountry(countryRepository.findOne(id)); 
        
        return new ResponseEntity<List<State>>(states, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/cities/list/{id}", method = GET)
    public ResponseEntity<List<City>> getCitiesList(@PathVariable(value = "id") Long id) {
        
        List<City> cities = cityRepository.findByState(stateRepository.findOne(id)); 
        
        return new ResponseEntity<List<City>>(cities, HttpStatus.OK);
    }
}
