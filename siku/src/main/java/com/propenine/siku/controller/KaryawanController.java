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

import com.propenine.siku.dto.karyawan.KaryawanMapper;
import com.propenine.siku.dto.karyawan.request.EditKaryawanReqDTO;
import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.KaryawanService;

import jakarta.validation.Valid;

@Controller
public class KaryawanController {  
    @Autowired
    private KaryawanService karyawanService;

    @Autowired
    private KaryawanMapper karyawanMapper;

    // @GetMapping("/karyawan/viewall")
    // public String listKaryawan(Model model) {
    //     List<User> listUser = karyawanService.getAllKaryawan();

    //     System.out.println("Number of users: " + listUser.size());

    //     for (User user : listUser) {
    //         System.out.println(user); // Assuming User class has overridden toString method
    //     }

    //     model.addAttribute("listUser", listUser);
    //     return "karyawan/viewall-karyawan";
    // }

    @GetMapping("/karyawan/viewall")
    public String listKaryawan(@RequestParam(name = "role", required = false) String role, Model model) {
        List<User> listUser;

        if (role != null && !role.isEmpty()) {
            // If role parameter is provided, filter users by role
            listUser = karyawanService.findByRoleContainingIgnoreCase(role);
        } else {
            // Otherwise, get all users
            listUser = karyawanService.getAllKaryawan();
        }

        System.out.println("Number of users: " + listUser.size());

        for (User user : listUser) {
            System.out.println(user); // Assuming User class has overridden toString method
        }

        model.addAttribute("listUser", listUser);
        return "karyawan/viewall-karyawan";
    }



    @GetMapping("/search-karyawan")
    public String searchKaryawanByNama(
            @RequestParam(value = "nama_depan", required = false) String nama_depan,
            @RequestParam(value = "nama_belakang", required = false) String nama_belakang,
            Model model) {
        
        System.out.println("Searching for users with first name: " + nama_depan + ", last name: " + nama_belakang);

        List<User> karyawanFiltered = karyawanService.searchKaryawanByNama(nama_depan, nama_belakang);

        System.out.println("Number of users found: " + karyawanFiltered.size());
        for (User user : karyawanFiltered) {
            System.out.println(user);
        }
        model.addAttribute("listUser", karyawanFiltered);
        return "karyawan/viewall-karyawan";
    }


    // @GetMapping("/search-karyawan")
    // public String searchKaryawanByNama(
    //         @RequestParam(value = "query", required = false) String nama,
    //         @RequestParam(value = "nama_belakang", required = false) String nama_belakang,
    //         Model model) {
        
    //     List<User> karyawanFiltered = karyawanService.searchKaryawanByNama(nama_depan, nama_belakang);
    //     model.addAttribute("listUser", karyawanFiltered);
    //     return "viewall-karyawan"; // Sesuaikan dengan nama view yang akan digunakan
    // }


    // DETAIL
    @GetMapping("/karyawan/{id}")
    public String detailUser(@PathVariable("id") Long id, Model model) {
        User user = karyawanService.getUserById(id);
        model.addAttribute("user", user);
        return "karyawan/detail-karyawan";
    }

    // EDIT
    @GetMapping("/karyawan/edit/{id}")
    public String editKaryawan(@PathVariable("id") Long id, Model model) {
        User user = karyawanService.getUserById(id);
        model.addAttribute("user", user);
        return "karyawan/edit-karyawan"; // Assuming you have a view for editing user details
    }

    @PostMapping("/karyawan/edit/{id}")
    public String updateKaryawan(@PathVariable("id") Long id, @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        // Update the user details using the service
        user.setId(id); // Set the ID of the user to ensure correct update
        karyawanService.editKaryawan(user);
        redirectAttributes.addAttribute("id", id); // Pass the ID as a parameter in the redirect URL
        return "redirect:/karyawan/{id}"; // Redirect to the detail page for the updated user
    }




    // @GetMapping("karyawan/edit/{id}")
    // public String editKaryawanForm(@PathVariable("id") Long id, Model model) {
    //     // Retrieve the karyawan entity by its ID
    //     User user = karyawanService.getUserById(id);
    //     // Map the karyawan entity to the EditKaryawanReqDTO
    //     EditKaryawanReqDTO editKaryawanReqDTO = karyawanMapper.karyawanToEditKaryawanRequestDTO(user);
    //     // Add the EditKaryawanReqDTO to the model
    //     model.addAttribute("editKaryawanReqDTO", editKaryawanReqDTO);
    //     return "karyawan/edit-karyawan"; // Assuming you have a view for editing karyawan
    // }

    // @PostMapping("karyawan/edit/{id}")
    // public String processEditKaryawan(@Valid @ModelAttribute EditKaryawanReqDTO karyawanDTO, BindingResult bindingResult, Model model) {

    //     if (bindingResult.hasErrors()) {
    //         // Handle validation errors
    //         return "karyawan/edit-karyawan";
    //     }

    //     var karyawanFromDTO = karyawanMapper.editKaryawanRequestDTOToKaryawan(karyawanDTO);

    //     // Update the user details
    //     var updatedKaryawan = karyawanService.editKaryawan(karyawanFromDTO);

    //     System.out.println("Status Karyawan: " + karyawanDTO.getStatusKaryawan());

    //     // Redirect to the updated user details page
    //     return "redirect:/karyawan/" + updatedKaryawan.getId();
    // }

}
