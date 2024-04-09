package com.propenine.siku.controller;


import com.propenine.siku.model.User;
import com.propenine.siku.repository.UserRepository;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.UserService;


import jakarta.validation.Valid;


import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;




@Controller
public class UserController {
   @Autowired
   private UserService userService;


   @Autowired
   private AuthenticationService authenticationService;


   @Autowired
   UserRepository userRepository;


   @Autowired
   private PasswordEncoder passwordEncoder;


   @GetMapping("/register")
   public String register(Model model){
       User userRegister = new User();
       model.addAttribute("userRegister", userRegister);


       User user = authenticationService.getLoggedInUser();
       model.addAttribute("user", user);
       return "register";
   }


   @PostMapping("/registerUser")
       public String registerUser(@ModelAttribute("userRegister") @Valid User userRegister, BindingResult bindingResult, Model model){
           if (bindingResult.hasErrors()) {
               model.addAttribute("userRegister", userRegister);
               model.addAttribute("errors", bindingResult.getAllErrors());


               User user = authenticationService.getLoggedInUser();
               model.addAttribute("user", user);
               return "register";
           }


           if (userService.existsByUsername(userRegister.getUsername()) || userService.existsByEmail(userRegister.getEmail())) {
               // Handle duplicate username or email
               model.addAttribute("duplicateError", "Username atau email sudah terdaftar.");
               User user = authenticationService.getLoggedInUser();
               model.addAttribute("user", user);
               return "register";
           }


           User user = authenticationService.getLoggedInUser();
           model.addAttribute("user", user);
           userService.registerUser(userRegister);
           return "landing-page";
       }


   @GetMapping("/login")
   public String login(Model model){
       User user = new User();
       model.addAttribute("user", user);
       return "login";
   }


   @PostMapping("/loginUser")
   public String loginUser(@ModelAttribute("user") User user, Model model) {
       String username = user.getUsername();
       User userData = userRepository.findByUsername(username);


       if ( userData != null && user.getPassword().equals(userData.getPassword())) {
           authenticationService.addLoggedInUser(userData);


           User loggedInUser = authenticationService.getLoggedInUser();
           model.addAttribute("user", loggedInUser);


           return "landing-page";
       } else {
           model.addAttribute("error", "Invalid username or password");
           return "login";
       }
   }


   @PostMapping("/logout")
   public String logout() {
       authenticationService.removeLoggedInUser();
       return "redirect:/login";
   }


   @GetMapping("/no-access")
   public String showNoAccessPage() {
       return "no-access";
   }


   @GetMapping("/ex-hr-only")
   public String hr(Model model) {
       User loggedInUser = authenticationService.getLoggedInUser();
       model.addAttribute("user", loggedInUser);
       return "ex-hr-only";
   }


   @GetMapping("/view-profile")
   public String viewProfile(Model model) {
       User loggedInUser = authenticationService.getLoggedInUser();
       System.out.println(loggedInUser);


       model.addAttribute("user", loggedInUser);
       return "profile";
   }


   @GetMapping("/edit-profile")
   public String editProfile(Model model) {
       User loggedInUser = authenticationService.getLoggedInUser();
       if (loggedInUser == null) {
           return "redirect:/login";
       }

       model.addAttribute("user", loggedInUser);
       return "edit-profile";
   }

    @PostMapping("/success-edit-profile")
    public String updatedProfile(@Valid @ModelAttribute("user") User updatedUser,
                            BindingResult bindingResult,
                            Model model) {
    User loggedInUser = authenticationService.getLoggedInUser();
    if (loggedInUser == null) {
        return "redirect:/login";
    }

    if (bindingResult.hasErrors()) {
        return "edit-profile";
    }

    Long userId = loggedInUser.getId();
    if (userService.existsOtherUserWithSameUsername(userId, updatedUser.getUsername())) {
        model.addAttribute("duplicateError", "Username sudah digunakan oleh pengguna lain.");
        return "edit-profile";
    }

    if (userService.existsOtherUserWithSameEmail(userId, updatedUser.getEmail())) {
        model.addAttribute("duplicateError", "Email sudah digunakan oleh pengguna lain.");
        return "edit-profile";
    }

    loggedInUser.setUsername(updatedUser.getUsername());
    loggedInUser.setEmail(updatedUser.getEmail());
    loggedInUser.setNomor_hp(updatedUser.getNomor_hp());

    userService.editUserProfile(loggedInUser);

    model.addAttribute("success", true);

    return "redirect:/view-profile?success=profile";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "form-edit-password";
    }


    @PostMapping("/success-change-password")
    public String changePassword(@Valid @ModelAttribute("user") User loggedInUser,
                                @RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {

        User userFromDatabase = userRepository.findByUsername(loggedInUser.getUsername());

        if (userFromDatabase == null) {
            model.addAttribute("error", "User not found.");
            return "no-access";
        }

        String storedPassword = userFromDatabase.getPassword();

        if (currentPassword.isEmpty()) {
            model.addAttribute("errorCurrentPassword", "Current password cannot be empty.");
            return "form-edit-password";
        }

        if (!storedPassword.equals(currentPassword)) {
            model.addAttribute("errorCurrentPassword", "Current password is incorrect.");
            return "form-edit-password";
        }

        if (newPassword.isEmpty()) {
            model.addAttribute("errorNewPassword", "New password cannot be empty.");
            return "form-edit-password";
        }

        if (confirmPassword.isEmpty()) {
            model.addAttribute("errorConfirmPassword", "Confirm password cannot be empty.");
            return "form-edit-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorConfirmPassword", "New passwords do not match.");
            return "form-edit-password";
        }

        loggedInUser.setPassword(newPassword);

        userRepository.save(loggedInUser);

        System.out.println("success pass: " + (loggedInUser.getPassword()));

        model.addAttribute("success", true);

        return "redirect:/view-profile?success=password";
    }




}
