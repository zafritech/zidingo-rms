/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.zafritech.zidingorms.dao.PwdDao;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.services.UserService;

/**
 *
 * @author LukeS
 */
@Controller
public class UserRestController {
    
    @Autowired
    private UserService userService;
    
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
}
