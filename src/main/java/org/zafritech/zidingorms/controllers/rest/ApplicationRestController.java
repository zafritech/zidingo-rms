/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;

import java.util.List;
import org.zafritech.zidingorms.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.zafritech.zidingorms.dao.TaskDao;
import org.zafritech.zidingorms.domain.City;
import org.zafritech.zidingorms.domain.Country;
import org.zafritech.zidingorms.domain.State;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.CityRepository;
import org.zafritech.zidingorms.repositories.CountryRepository;
import org.zafritech.zidingorms.repositories.StateRepository;
import org.zafritech.zidingorms.services.GeneralService;
import org.zafritech.zidingorms.services.UserService;

/**
 *
 * @author LukeS
 */
@Controller
public class ApplicationRestController {
    
    @Autowired
    private GeneralService generalService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private StateRepository stateRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @RequestMapping("/api/login/check")
    public ResponseEntity<String> checkUserLogin(Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        return new ResponseEntity<String>(userName, HttpStatus.OK);
    }
    
    @RequestMapping("/api/users/list")
    public ResponseEntity<List<User>> getUserList(Model model) {
        
        List<User> users = userService.findAll();
        
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/tasks/new", method = POST)
    public ResponseEntity<Task> newTask(@RequestBody TaskDao taskDao) {
        
        Task task = generalService.createTask(taskDao);

        return new ResponseEntity<Task>(task, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/tasks/active", method = GET)
    public ResponseEntity<List<Task>> getUnreadMessages() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        
        List<Task> tasks = generalService.getActiveTasks(user);
        
        return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
    }
    
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

    @RequestMapping(value = "/api/user/byuuid/{uuid}", method = GET)
    public ResponseEntity<User> getUserByUuId(@PathVariable(value = "uuid") String uuid) {
        
        User user = userService.getByUuId(uuid); 
        user.setPassword(null); 
        
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
