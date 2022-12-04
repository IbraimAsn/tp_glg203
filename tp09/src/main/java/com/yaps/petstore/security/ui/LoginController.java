package com.yaps.petstore.security.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @RequestMapping("/login")
    public String login(@RequestParam(required = false, name = "error") String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }


    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
