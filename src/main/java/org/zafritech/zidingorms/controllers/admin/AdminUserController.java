package org.zafritech.zidingorms.controllers.admin;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zafritech.zidingorms.dao.ClaimDao;
import org.zafritech.zidingorms.dao.UserDao;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Claim;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ClaimRepository;
import org.zafritech.zidingorms.repositories.ProjectRepository;
import org.zafritech.zidingorms.repositories.RoleRepository;
import org.zafritech.zidingorms.services.UserService;

@Controller
@Secured({"ROLE_ADMIN"})
public class AdminUserController {

    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    @RequestMapping(value = {"/admin/users", "/admin/users/list"})
    public String listUsers(Model model) {

        model.addAttribute("users", userService.findAll());

        return "admin/users/list";
    }

    @RequestMapping("/admin/users/{uuid}")
    public String getUser(@PathVariable String uuid, Model model) {

        model.addAttribute("profile", userService.getByUuId(uuid));
        model.addAttribute("rolesList", roleRepository.findAll());

        return "admin/users/profile";
    }

    @RequestMapping("/admin/users/claims/{uuid}")
    public String getUserClaims(@PathVariable String uuid, Model model) {

        User user = userService.getByUuId(uuid);
        
        List<Claim> userClaims = claimRepository.findByUser(user);
        
        List<ClaimDao> claims = new ArrayList<>();
        
        for (Claim userClaim : userClaims) {
            
            ClaimDao dao = new ClaimDao();
            dao.setUserClaimType(userClaim.getClaimType().name());
            dao.setUserClaimValue(userClaim.getClaimValue());
            
            String str = userClaim.getClaimType().name();
            
            if (str.substring(0, str.indexOf("_")).equals("PROJECT")) {
                
                Project proj = projectRepository.findOne(Long.parseLong(userClaim.getClaimValue()));
                dao.setUserClaimStringValue(proj.getProjectShortName());
                
            } else if (str.substring(0, str.indexOf("_")).equals("DOCUMENT")) {
                
                Artifact doc = artifactRepository.findOne(Long.parseLong(userClaim.getClaimValue()));
                dao.setUserClaimStringValue(doc.getArtifactName());
            }
            
            claims.add(dao);
        }
        
        model.addAttribute("user", userService.getByUuId(uuid));
        model.addAttribute("claims", claims); 

        return "admin/users/claims";
    }

    @RequestMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {

        model.addAttribute("user", userService.findById(id));

        return "admin/users/userform";
    }

    @RequestMapping(value = "/admin/users/new", method = RequestMethod.GET)
    public String createUser(Model model) {

        UserDao user = new UserDao();
        model.addAttribute("user", user);

        return "/admin/users/new";
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") @Valid UserDao userDao,
            BindingResult bindingResult,
            Model model) throws Exception {

        if (!userService.passwordAndConfirmationMatch(userDao)) {

            throw new Exception("The supplied password and confirmation do not match.");
        }

        if (userService.userExists(userDao.getEmail())) {

            throw new Exception("A user with email " + userDao.getEmail() + " already exists.");
        }

        if (bindingResult.hasErrors()) {

            return "/admin/users/new";
        }

        User user = userService.saveDao(userDao);

        model.addAttribute("user", user);
        return "redirect:/admin/users/" + userService.findByEmail(user.getEmail()).getUuId();
    }

    @RequestMapping("/admin/users/delete/{id}")
    public String delete(@PathVariable Long id) {

        userService.deleteUser(id);

        return "redirect:/admin/users/list";
    }
}
