/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.commons.enums.ClaimType;
import org.zafritech.zidingorms.database.dao.ClaimDao;
import org.zafritech.zidingorms.database.dao.UserDao;
import org.zafritech.zidingorms.database.dao.converter.DaoToUserConverter;
import org.zafritech.zidingorms.database.domain.Claim;
import org.zafritech.zidingorms.database.domain.Role;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ClaimRepository;
import org.zafritech.zidingorms.database.repositories.UserRepository;

/**
 *
 * @author LukeS
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    private DaoToUserConverter daoToUser;

    @Autowired
    private ClaimRepository claimRepository;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }
     
    @Override 
    public List<User> allUser() {
        
        return new ArrayList(userRepository.findAll());
    }
    
    @Override
    public User loggedInUser() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String name = userDetails.getUsername();
        
        User user = userRepository.findByUserName(name);

        return user;
    }
    
    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();
        List<User> sanitizedUsers = new ArrayList<>();

        userRepository.findAll().forEach(users::add);
        
        users.forEach(user->{
            
            user.setUserName(null); 
            user.setPassword(null); 
            sanitizedUsers.add(user);
            
        });

        return sanitizedUsers;
    }

    @Override
    public List<User> findOrderByFirstName() {

        List<User> users = new ArrayList<>();
        List<User> sanitizedUsers = new ArrayList<>();

        userRepository.findAllByOrderByFirstNameAsc().forEach(users::add);
        
        users.forEach(user->{
            
            user.setUserName(null); 
            user.setPassword(null); 
            sanitizedUsers.add(user);
            
        });

        return sanitizedUsers;
    }
    
    @Override
    public List<User> findOrderByFirstName(int pageSize, int pageNumber) {
        
        List<User> users = new ArrayList<>();
        List<User> sanitizedUsers = new ArrayList<>();
        PageRequest request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "firstName");
        
        userRepository.findAll(request).forEach(users::add);
        
        users.forEach(user->{
            
            user.setUserName(null); 
            user.setPassword(null); 
            sanitizedUsers.add(user);
            
        });
        
        return sanitizedUsers;
    }
    

    @Override
    public User findByUserName(String name) {

        return userRepository.findByEmail(name);
    }

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if (user == null) {

            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }

    @Override
    public User findById(Long id) {

        return userRepository.findOne(id);
    }

    @Override
    public User getByUuId(String uuid) {

        return userRepository.getByUuId(uuid);
    }

    @Override
    public User saveUser(User user) {

        if (userExists(user.getEmail())) {
            return null;
        }

        User newUser = new User(user.getEmail(), user.getPassword(), (HashSet<Role>) user.getUserRoles());

        return userRepository.save(newUser);
    }

    @Override
    public User saveDao(UserDao userDao) {

        if (userDao != null) {

            return userRepository.save(daoToUser.convert(userDao));
        }

        return null;
    }

    @Override
    public void deleteUser(Long id) {

        userRepository.delete(id);
    }

    @Override
    public boolean passwordAndConfirmationMatch(UserDao userDao) {

        if (userDao.getPassword().equals(userDao.getConfirmPassword())) {
            return true;
        }

        return false;
    }

    @Override
    public boolean userExists(String email) {

        if (userRepository.findByEmail(email) != null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean passwordMatches(String rawPassword, String encodedPassword) {

        return new BCryptPasswordEncoder().matches(rawPassword, rawPassword); 
    }

    @Override
    public User changePasswordTo(User user, String password) {

        user.setPassword(new BCryptPasswordEncoder().encode(password)); 
        
        return user;
    }
    
    @Override
    public  List<Integer> getPagesList(int currentPage, int lastPage) {
        
        List<Integer> pageList = new ArrayList<>();

        int startIndex = 1;
        int upperLimit = 1;
        
        if (lastPage < 9) {
            
            startIndex = 1;
            upperLimit = lastPage;
            
        } else {

            upperLimit = ((int) Math.ceil((double)currentPage / 9) * 9);
            upperLimit = (lastPage < upperLimit) ? lastPage : upperLimit;
            startIndex = upperLimit - 8;
        
        }
        
        for (int i = startIndex; i <= upperLimit; i++) {

            pageList.add(i);
        }
        
        return pageList;
    }
    
    @Override
    public Claim createClaim(ClaimDao claimDao) {
        
        User user = userRepository.getByUuId(claimDao.getUserUuId());
        
        Claim claim = new Claim(user, ClaimType.valueOf(claimDao.getUserClaimType()), claimDao.getUserClaimValue());
        
        return claimRepository.save(claim);
    }

    @Override
    public List<Claim> findUserClaims(User user) {

        List<Claim> claims = claimRepository.findByUser(user);
                
        return claims;
    }

}
