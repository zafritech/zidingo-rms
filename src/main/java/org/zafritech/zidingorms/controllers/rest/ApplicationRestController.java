/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.zafritech.zidingorms.commons.enums.ClaimType;
import org.zafritech.zidingorms.dao.ClaimDao;
import org.zafritech.zidingorms.dao.RoleDao;
import org.zafritech.zidingorms.dao.TaskDao;
import org.zafritech.zidingorms.domain.City;
import org.zafritech.zidingorms.domain.Claim;
import org.zafritech.zidingorms.domain.Country;
import org.zafritech.zidingorms.domain.Role;
import org.zafritech.zidingorms.domain.State;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.CityRepository;
import org.zafritech.zidingorms.repositories.CountryRepository;
import org.zafritech.zidingorms.repositories.RoleRepository;
import org.zafritech.zidingorms.repositories.StateRepository;
import org.zafritech.zidingorms.repositories.UserRepository;
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
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
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

    @RequestMapping(value = "/api/user/roles/{uuid}", method = GET)
    public ResponseEntity<List<Role>> getUserRoles(@PathVariable(value = "uuid") String uuid) {
        
        User user = userService.getByUuId(uuid); 
        List<Role> roles = new ArrayList(user.getUserRoles());
        
        return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user/roles/all", method = GET)
    public ResponseEntity<List<Role>> getAllRoles() {
        
        Iterable<Role> rolesIterable = roleRepository.findAll();
        List<Role> roles = new ArrayList<>();
        rolesIterable.forEach(roles::add);
        
        return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/roles/add/{uuid}", method = POST)
    public ResponseEntity<List<Role>> addUserRoles(@RequestBody List<RoleDao> roleDaos, @PathVariable(value = "uuid") String uuid) {
        
        List<Role> newRoles = new ArrayList<>();
        
        for (RoleDao roleDao : roleDaos) {
            
            newRoles.add(roleRepository.findByRoleName(roleDao.getRoleName()));
        }
        
        User user = userService.getByUuId(uuid); 
        user.setUserRoles(new HashSet(newRoles)); 
        userRepository.save(user);

        return new ResponseEntity<List<Role>>(newRoles, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/claims/claimtypes", method = RequestMethod.GET)
    public ResponseEntity<List<ClaimType>> getClaimTypes() {
        
        return new ResponseEntity<List<ClaimType>>(Arrays.asList(ClaimType.values()), HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/api/claims/new", method = POST)
    public ResponseEntity<Claim> newClaim(@RequestBody ClaimDao claimDao) {
        
        Claim claim = generalService.createClaim(claimDao);

        return new ResponseEntity<Claim>(claim, HttpStatus.OK);
    }
}
