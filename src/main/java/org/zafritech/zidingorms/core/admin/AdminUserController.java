package org.zafritech.zidingorms.core.admin;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.zafritech.zidingorms.database.dao.ClaimDao;
import org.zafritech.zidingorms.database.dao.UserDao;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Claim;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.ClaimRepository;
import org.zafritech.zidingorms.database.repositories.ItemCategoryRepository;
import org.zafritech.zidingorms.database.repositories.ProjectRepository;
import org.zafritech.zidingorms.database.repositories.RoleRepository;
import org.zafritech.zidingorms.database.repositories.UserRepository;
import org.zafritech.zidingorms.core.user.UserService;

@Controller
@Secured({"ROLE_ADMIN"})
public class AdminUserController {

    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private ItemCategoryRepository ItemCategoryRepository;
    
    @Autowired
    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    @RequestMapping(value = {"/admin/users", "/admin/users/list"})
    public String listUsers(@RequestParam(name = "s", defaultValue = "10") int pageSize,
                            @RequestParam(name = "p", defaultValue = "1") int pageNumber,
                            Model model) {
        
        Integer usersCount = userRepository.findAll().size();
        Integer pageCount = (int)Math.ceil((float)(usersCount / pageSize)) + 1;
        List<Integer> pageList = userService.getPagesList(pageNumber, pageCount);

        model.addAttribute("users", userService.findOrderByFirstName(pageSize, pageNumber));
        model.addAttribute("userCount", usersCount);
        model.addAttribute("page", pageNumber);
        model.addAttribute("size", pageSize);
        model.addAttribute("list", pageList);
        model.addAttribute("count", pageCount);
        model.addAttribute("last", Collections.max(pageList));

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
            dao.setClaimId(userClaim.getId()); 
            dao.setUserClaimType(userClaim.getClaimType().name());
            dao.setUserClaimValue(userClaim.getClaimValue());
            
            String str = userClaim.getClaimType().name();
            
            switch (str.substring(0, str.indexOf("_"))) {
                
                case "PROJECT":
                    Project proj = projectRepository.findOne(Long.parseLong(userClaim.getClaimValue()));
                    dao.setUserClaimStringValue(proj.getProjectShortName());
                    break;
                    
                case "DOCUMENT":
                    Artifact doc = artifactRepository.findOne(Long.parseLong(userClaim.getClaimValue()));
                    dao.setUserClaimStringValue(doc.getArtifactName());
                    break;
                    
                case "CATEGORY":
                    ItemCategory category = ItemCategoryRepository.findOne(Long.parseLong(userClaim.getClaimValue()));
                    dao.setUserClaimStringValue(category.getCategoryName());
                    break;
                    
                default:
                    break;
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

        return "admin/users/new";
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

            return "admin/users/new";
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
