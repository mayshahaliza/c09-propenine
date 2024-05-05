package com.propenine.siku.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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

import com.propenine.siku.model.Pesanan;
import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.KaryawanService;
import com.propenine.siku.service.PesananService;

import jakarta.validation.Valid;

@Controller
public class KaryawanController {

    @Autowired
    private KaryawanService karyawanService;

    @Autowired
    private PesananService pesananService;

    @Autowired
    private AuthenticationService authenticationService;

    // VIEW ALL DATA KARYAWAN & SEARCH BY FILTER ROLE
    @GetMapping("/karyawan/viewall")
    public String listKaryawan(@RequestParam(name = "role", required = false) String role, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<User> listKaryawan;

        if (role != null && !role.isEmpty()) {
            listKaryawan = karyawanService.findByRoleContainingIgnoreCase(role);
        } else {
            listKaryawan = karyawanService.getAllKaryawan();
        }

        model.addAttribute("listKaryawan", listKaryawan);
        model.addAttribute("user", loggedInUser);
        return "karyawan/viewall-karyawan";
    }

    // SEARCH BY NAME
    @GetMapping("/search-karyawan")
    public String searchKaryawan(@RequestParam("name") String name, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<User> searchResults = karyawanService.searchByName(name);

        model.addAttribute("listKaryawan", searchResults);
        model.addAttribute("user", loggedInUser);

        return "karyawan/viewall-karyawan";
    }

    // DETAIL
    @GetMapping("/karyawan/{id}")
    public String detailUser(@PathVariable("id") Long id, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User karyawan = karyawanService.getUserById(id);
        model.addAttribute("karyawan", karyawan);
        model.addAttribute("user", loggedInUser);
        return "karyawan/detail-karyawan";
    }

    // EDIT
    @GetMapping("/karyawan/edit/{id}")
    public String editKaryawan(@PathVariable("id") Long id, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();

        if (loggedInUser == null) {
            return "redirect:/login";
        }

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

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        
        var karyawan = karyawanService.getUserById(id);
        karyawanService.deleteKaryawan(karyawan);

        model.addAttribute("user", loggedInUser);

        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");

        return "redirect:/karyawan/viewall";
    }

    // VIEW PERFORMANCE
    @GetMapping("/karyawan/performance/{id}")
    public String performance(@PathVariable("id") Long id, @RequestParam(required = false) Integer bulan,
                            @RequestParam(required = false) Integer tahun, @RequestParam(required = false) String statusPesanan,Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        var karyawan = karyawanService.getUserById(id);  

        List<Pesanan> pesananList;

        if (bulan != null && tahun != null) {
            System.out.println("Filtering by Month and Year: " + bulan + "/" + tahun);
            pesananList = pesananService.findOrdersByUserIdAndMonthAndYear(id, bulan, tahun);
        } 
        else if (statusPesanan != null && !statusPesanan.isEmpty()) {
            System.out.println("Filtering by Status Pesanan: " + statusPesanan);
            pesananList = pesananService.getPesananByUserIdAndStatus(id, statusPesanan);
        } 
        else {
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();
            int currentYear = currentDate.getYear();
            System.out.println("No filter applied. Using current month and year: " + currentMonth + "/" + currentYear);
            pesananList = pesananService.findOrdersByUserIdAndMonthAndYear(id, currentMonth, currentYear);
        }

        pesananList.forEach(pesanan -> {
            int totalCost = pesanan.getProduct().getHarga() * pesanan.getJumlahBarangPesanan();
            pesanan.setJumlahBiayaPesanan(totalCost);
        });

        pesananList.sort(Comparator.comparing(Pesanan::getStatusPesanan,
                Comparator.comparing(status -> {
                    switch (status) {
                        case "Ongoing":
                            return 0;
                        case "Complete":
                            return 1;
                        case "Canceled":
                            return 2;
                        default:
                            return 3;
                    }
                })));

        model.addAttribute("pesananList", pesananList);
        model.addAttribute("karyawan", karyawan);
        model.addAttribute("user", loggedInUser);


        System.out.println("Pesanan List Size: " + pesananList.size());

        for (Pesanan pesanan : pesananList) {
            System.out.println(pesanan);

        }        
            
        System.out.println("\nNumber of orders found: " + pesananList.size()); // Print the number of orders found

        return "karyawan/agent-performance";
    }


}


