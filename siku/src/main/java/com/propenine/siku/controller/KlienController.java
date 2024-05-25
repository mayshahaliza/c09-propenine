package com.propenine.siku.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.propenine.siku.dto.klien.CreateKlienRequestDTO;
import com.propenine.siku.dto.klien.KlienMapper;
import com.propenine.siku.model.Klien;
import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.PesananService;
import com.propenine.siku.service.klien.KlienService;

import jakarta.validation.Valid;

@Controller
public class KlienController {
    @Autowired
    private AuthenticationService authenticationService; 
    
    @Autowired
    private KlienMapper klienMapper;

    @Autowired
    private KlienService klienService;

    @Autowired
    private PesananService pesananService;

    //CREATE KLIEN
    @GetMapping("klien/tambah")
    public String formAddKlien(Model model) {
        var klienDTO = new CreateKlienRequestDTO();
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        model.addAttribute("klienDTO", klienDTO);
        return "klien/form-create-klien";
    }

    @PostMapping("/klien/tambah")
    public String addKlien(@ModelAttribute("klienDTO") @Valid CreateKlienRequestDTO klienDTO,
                            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            // Jika terdapat kesalahan validasi, kembali ke halaman formulir
            return "klien/form-create-klien";
        }

        var klien = klienMapper.createKlienRequestDTOToKlien(klienDTO);
        klienService.saveKlien(klien);

        // Set klienAdded ke true untuk menunjukkan bahwa klien telah ditambahkan
        model.addAttribute("klienAdded", true);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "klien/form-create-klien";
    }



    // VIEWALL KLIEN
    @GetMapping("klien")
    public String listKlien(Model model){
        var listKlien = klienService.getAllKlien();
        model.addAttribute("listKlien", listKlien);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "klien/viewall-klien";
    }

    // VIEW KLIEN
    @GetMapping("/klien/{id}")
    public String detailKlien(@PathVariable("id") UUID id, Model model) {
        var klien = klienService.getKlienById(id);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        model.addAttribute("klien", klien);
        return "klien/view-klien";
    }

    // UPDATE KLIEN
    @GetMapping("/klien/update/{id}")
    public String formUpdateKlien(@PathVariable("id") UUID id, Model model){
        var klien = klienService.getKlienById(id);
        var klienDTO = klienMapper.klienToUpdateKlienRequestDTO(klien);
        klienDTO.setId(klien.getId());

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        model.addAttribute("klienDTO", klienDTO);
        model.addAttribute("klien", klien);

        return "klien/form-update-klien";

    }

    @PostMapping("/klien/update/{id}")
    public String updateKlien(@PathVariable("id") UUID id,
                            @ModelAttribute("klienDTO") @Valid CreateKlienRequestDTO klienDTO,
                            Model model) {
    
        Klien existingKlien = klienService.getKlienById(id);
    
        existingKlien.setNamaKlien(klienDTO.getNamaKlien());
        existingKlien.setRumahSakit(klienDTO.getRumahSakit());
        existingKlien.setEmailKlien(klienDTO.getEmailKlien());
        existingKlien.setNoHpKlien(klienDTO.getNoHpKlien());
        existingKlien.setAlamatKlien(klienDTO.getAlamatKlien());
    
        klienService.saveKlien(existingKlien);
    
        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
    
        model.addAttribute("id", existingKlien.getId());
        model.addAttribute("namaKlien", existingKlien.getNamaKlien());
    
        // Menambahkan parameter success=status untuk menampilkan modal
        return "redirect:/klien/{id}?success=status";
    }
    
    

    //DELETE KLIEN
    @GetMapping("klien/delete/{id}")
    public String deleteKlien(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes, Model model) {
        var klien = klienService.getKlienById(id);
        
        // Memanggil metode untuk menghapus pesanan-pesanan terkait
        pesananService.deletePesananByKlien(klien);

        // Menghapus klien
        klienService.deleteKlien(klien);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        model.addAttribute("namaKlien", klien.getNamaKlien()); 
        redirectAttributes.addFlashAttribute("successMessage", "Client deleted successfully.");
        return "redirect:/klien";
    }

    //SEARCH KLIEN BY NAMA
    @GetMapping("/search")
    public String filteredByNama(@RequestParam(value = "query", required = false) String namaKlien,
                                @RequestParam(value = "queryRumahSakit", required = false) String rumahSakit,
                                Model model) {
        List<Klien> listKlienFiltered = klienService.listKlienFiltered(namaKlien, rumahSakit);
        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        
        model.addAttribute("listKlien", listKlienFiltered);
        return "klien/viewall-klien";
    }
}
