package com.propenine.siku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.KaryawanService;

import jakarta.validation.Valid;

@Controller
public class KaryawanController {

    @Autowired
    private KaryawanService karyawanService;

    @Autowired
    private AuthenticationService authenticationService;

    // VIEW ALL DATA KARYAWAN & SEARCH BY FILTER ROLE
    @GetMapping("/karyawan/viewall")
    public String listKaryawan(@RequestParam(name = "role", required = false) String role, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        List<User> listKaryawan;

        if (role != null && !role.isEmpty()) {
            listKaryawan = karyawanService.findByRoleContainingIgnoreCase(role);
        } else {
            listKaryawan = karyawanService.getAllKaryawan();
        }

        System.out.println("Number of users: " + listKaryawan.size());

        for (User karyawan : listKaryawan) {
            System.out.println(karyawan);
        }

        model.addAttribute("listKaryawan", listKaryawan);
        model.addAttribute("user", loggedInUser);
        return "karyawan/viewall-karyawan";
    }

    // SEARCH BY NAME
    @GetMapping("/search-karyawan")
    public String searchKaryawan(@RequestParam("name") String name, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        List<User> searchResults = karyawanService.searchByName(name);

        model.addAttribute("listKaryawan", searchResults);
        model.addAttribute("user", loggedInUser);

        return "karyawan/viewall-karyawan";
    }

    // DETAIL
    @GetMapping("/karyawan/{id}")
    public String detailUser(@PathVariable("id") Long id, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        User karyawan = karyawanService.getUserById(id);
        model.addAttribute("karyawan", karyawan);
        model.addAttribute("user", loggedInUser);
        return "karyawan/detail-karyawan";
    }

    // EDIT
    @GetMapping("/karyawan/edit/{id}")
    public String editKaryawan(@PathVariable("id") Long id, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        User karyawan = karyawanService.getUserById(id);
        model.addAttribute("karyawan", karyawan);
        model.addAttribute("user", loggedInUser);
        return "karyawan/edit-karyawan";
    }

    @PostMapping("/karyawan/edit/{id}")
    public String updateKaryawan(@PathVariable("id") Long id, @ModelAttribute("user") User karyawan, RedirectAttributes redirectAttributes) {
        karyawan.setId(id);
        karyawanService.editKaryawan(karyawan);
        redirectAttributes.addAttribute("success", "status");
        return "redirect:/karyawan/{id}";
    }

    // SOFT DELETE
    @GetMapping("/karyawan/delete/{id}")
    public String deleteKaryawan(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        var karyawan = karyawanService.getUserById(id);
        karyawanService.deleteKaryawan(karyawan);

        model.addAttribute("user", loggedInUser);

        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");

        return "redirect:/karyawan/viewall";
    }
}


