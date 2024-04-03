package com.io.mountblue.calendlyclone.controller;

import com.io.mountblue.calendlyclone.service.UserService;
import com.io.mountblue.calendlyclone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showMyLoginPage(){
        return "user-login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied(){
        return "access-denied";
    }

    @GetMapping("/registerForm")
    public String showRegistrationForm(Model themodel){
        User user=new User();
        themodel.addAttribute("user",user);
        themodel.addAttribute("emailExists", false);
        return "register-form";
    }

    @PostMapping("/saveUser")
    public String saveUserInDataBase(@ModelAttribute("user") User theuser, Model themodel){
        User user = userService.findUserByEmail(theuser.getEmail());
        if(user != null){
            themodel.addAttribute("emailExists",true);
            return "register-form";
        }
        userService.save(theuser);
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(){
        return "dashboard";
    }
}
