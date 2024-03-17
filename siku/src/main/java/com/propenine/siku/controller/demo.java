package com.propenine.siku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;

@Controller
public class demo {  
  @Autowired
  private AuthenticationService authenticationService;

    @GetMapping("/")
     public String landingPage(Model model){
     User loggedInUser = authenticationService.getLoggedInUser();
      model.addAttribute("user", loggedInUser);
    return "landing-page";}
}
