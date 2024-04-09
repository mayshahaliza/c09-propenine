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
               model.addAttribute("userRegister", userRegister);
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


   // @PostMapping("/success-edit-profile")
   // public String updatedProfile(@Valid @ModelAttribute("user") User updatedUser,
   //                         BindingResult bindingResult,
   //                         Model model) {
   // User loggedInUser = authenticationService.getLoggedInUser();
   // if (loggedInUser == null) {
   //     return "redirect:/login";
   // }


   // if (bindingResult.hasErrors()) {
   //     return "edit-profile";
   // }


   // loggedInUser.setUsername(updatedUser.getUsername());
   // loggedInUser.setEmail(updatedUser.getEmail());
   // loggedInUser.setNomor_hp(updatedUser.getNomor_hp());


   // userService.editUserProfile(loggedInUser);


   // return "redirect:/view-profile";
   // }


@PostMapping("/success-edit-profile")
public String updatedProfile(@Valid @ModelAttribute("user") User updatedUser,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
   User loggedInUser = authenticationService.getLoggedInUser();
   if (loggedInUser == null) {
       return "redirect:/login";
   }


   // Check if the username or email has changed
   if (!loggedInUser.getUsername().equals(updatedUser.getUsername())) {
       if (userService.existsByUsername(updatedUser.getUsername())) {
           // Username already exists for another user, add error message and redirect
           bindingResult.rejectValue("username", "error.user", "Username sudah digunakan oleh pengguna lain");
           redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
           return "redirect:/edit-profile";
       }
   }


   if (!loggedInUser.getEmail().equals(updatedUser.getEmail())) {
       if (userService.existsByEmail(updatedUser.getEmail())) {
           // Email already exists for another user, add error message and redirect
           bindingResult.rejectValue("email", "error.user", "Email sudah digunakan oleh pengguna lain");
           redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
           return "redirect:/edit-profile";
       }
   }


   // Perform other validations here...


   // If there are validation errors, redirect back to the edit profile page with errors
   if (bindingResult.hasErrors()) {
       redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
       return "redirect:/edit-profile";
   }


   // Update user profile if all validations pass
   // Copy data from updatedUser to loggedInUser
   loggedInUser.setUsername(updatedUser.getUsername());
   loggedInUser.setEmail(updatedUser.getEmail());
   loggedInUser.setNomor_hp(updatedUser.getNomor_hp());


   // Save changes to the user profile
   userService.editUserProfile(loggedInUser);


   // Add success flash attribute and redirect to view profile page
   redirectAttributes.addFlashAttribute("success", true);
   return "redirect:/view-profile";
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
           // Handle case when user is not found
           model.addAttribute("error", "User not found.");
           return "no-access";
       }


       // Retrieve the stored password from the user fetched from the database
       String storedPassword = userFromDatabase.getPassword();


       if (!storedPassword.equals(currentPassword)) {
           model.addAttribute("error", "Current password is incorrect.");
           System.out.println("current: " + currentPassword);
           System.out.println("stored: " + storedPassword);
           return "form-edit-password";
       }


       if (!newPassword.equals(confirmPassword)) {
           model.addAttribute("error", "New passwords do not match.");
           return "form-edit-password";
       }


       loggedInUser.setPassword(newPassword);


       userService.editUserProfile(loggedInUser);


       System.out.println("success pass: " + (loggedInUser.getPassword()));


       return "success-change-password";
   }


}
