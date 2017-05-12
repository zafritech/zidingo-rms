/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
import org.zafritech.zidingorms.core.commons.enums.ClaimType;
import org.zafritech.zidingorms.database.dao.PwdDao;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.dao.ClaimDao;
import org.zafritech.zidingorms.database.dao.RoleDao;
import org.zafritech.zidingorms.database.dao.UserEditDao;
import org.zafritech.zidingorms.database.domain.Claim;
import org.zafritech.zidingorms.database.domain.Role;
import org.zafritech.zidingorms.database.repositories.ClaimRepository;
import org.zafritech.zidingorms.database.repositories.RoleRepository;
import org.zafritech.zidingorms.database.repositories.UserRepository;

/**
 *
 * @author LukeS
 */
@Controller
public class UserRestController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
     
    @Autowired
    private ClaimRepository claimRepository;
    
    @RequestMapping("/api/login/check")
    public ResponseEntity<String> checkUserLogin(Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        return new ResponseEntity<String>(userName, HttpStatus.OK);
    }
    
    @RequestMapping("/api/users/list")
    public ResponseEntity<List<User>> getUserList(Model model) {
        
        List<User> users = userService.findOrderByFirstName();
        
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/password/change/{uuid}", method = POST)
    public ResponseEntity<User> getUserByUuId(@RequestBody PwdDao pwdDao, 
                                              @PathVariable(value = "uuid") String uuid) {
        
        User user = userService.getByUuId(uuid); 

        if (user != null && pwdDao.getNewPassword().equals(pwdDao.getNewPasswordConfirm()) 
                         && userService.passwordMatches(pwdDao.getNewPassword(), user.getPassword())) {
            
            
            userService.changePasswordTo(user, pwdDao.getNewPassword());
        }
        
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/byuuid/{uuid}", method = GET)
    public ResponseEntity<User> getUserByUuId(@PathVariable(value = "uuid") String uuid) {
        
        User user = userService.getByUuId(uuid); 
        user.setPassword(null); 
        
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user/update/{uuid}", method = POST)
    public ResponseEntity<User> getUserUpdate(@RequestBody UserEditDao userDao, 
                                              @PathVariable(value = "uuid") String uuid) {
        
        User user = userService.getByUuId(uuid); 
        
        if (userDao.getFirstName() != null && !userDao.getFirstName().isEmpty()) { user.setFirstName(userDao.getFirstName()); }
        if (userDao.getLastName() != null && !userDao.getLastName().isEmpty()) { user.setLastName(userDao.getLastName()); }
        if (userDao.getMainRole() != null && !userDao.getMainRole().isEmpty()) { user.setMainRole(userDao.getMainRole()); }
        if (userDao.getPhoneNumber() != null && !userDao.getPhoneNumber().isEmpty()) { user.setPhoneNumber(userDao.getPhoneNumber()); }
        if (userDao.getMobileNumber() != null && !userDao.getMobileNumber().isEmpty()) { user.setMobileNumber(userDao.getMobileNumber()); }
        if (userDao.getAddress() != null && !userDao.getAddress().isEmpty()) { user.setAddress(userDao.getAddress()); }
        if (userDao.getCountry() != null && !userDao.getCountry().isEmpty()) { user.setCountry(userDao.getCountry()); }
        if (userDao.getState() != null && !userDao.getState().isEmpty()) { user.setState(userDao.getState()); }
        if (userDao.getCity() != null && !userDao.getCity().isEmpty()) { user.setCity(userDao.getCity()); }
        if (userDao.getPostCode()!= null && !userDao.getPostCode().isEmpty()) { user.setPostCode(userDao.getPostCode()); }
        
        userRepository.save(user);
        
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/users/delete/{uuid}", method = GET)
    public ResponseEntity<Long> deleteUser(@PathVariable(value = "uuid") String uuid) {
        
        User user = userService.getByUuId(uuid);
        Long userId = user.getId();
        userRepository.delete(user); 

        return new ResponseEntity<Long>(userId, HttpStatus.OK);
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
        
        Claim claim = userService.createClaim(claimDao);

        return new ResponseEntity<Claim>(claim, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/claims/delete/{id}", method = GET)
    public ResponseEntity<Long> deleteClaim(@PathVariable(value = "id") Long id) {
        
        claimRepository.delete(id);

        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }
}
