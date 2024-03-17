package com.propenine.siku.controllerstok;
import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.katalog.KategoriService;
import com.propenine.siku.servicestok.StokService;

import jakarta.validation.Valid;

import com.propenine.siku.dtostok.request.CreateStokRequestDTO;
import com.propenine.siku.dtostok.request.UpdateStokRequestDTO;
import com.propenine.siku.modelstok.Stok;
import com.propenine.siku.dtostok.StokMapper;
import com.propenine.siku.repositorystok.StokDb;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

//import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StokController {
    @Autowired
    KategoriService kategoriService;

    // Autentikasi
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private StokService stokService;

    @Autowired
    private StokMapper stokMapper;

    @GetMapping("stok")
    public String listGudang(Model model){
        var listStok = stokService.getAllStok();
        model.addAttribute("listStok", listStok);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "viewall-stok";
    }
    
    @GetMapping("stok/tambah")
    public String formAddStok(Model model){
        var stokDTO = new CreateStokRequestDTO();
        var allKategori = kategoriService.getAllKategori();
        model.addAttribute("allKategori", allKategori);
        model.addAttribute("stokDTO", stokDTO);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "form-tambah-stok";
    }

    @PostMapping("stok/tambah")
    public String addStok(@Valid @ModelAttribute CreateStokRequestDTO stokDTO, Model model){
        if (stokDTO.getStok() > 0) {
            stokDTO.setStatus(true);
        } else {
            stokDTO.setStatus(false);
        }
        var stok = stokMapper.createStokRequestDTOToStok(stokDTO);

        stokService.createStok(stok);

        model.addAttribute("stok", stokDTO);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "success-create-stok";
    }

    @GetMapping("stok/update/{idKatalog}")
    public String formUpdateStok(@PathVariable("idKatalog") UUID idKatalog, Model model) {
        var stok = stokService.getStokById(idKatalog);
        var stokDTO = stokMapper.stokToUpdateStokRequestDTO(stok);
        var allKategori = kategoriService.getAllKategori();

        model.addAttribute("stokDTO", stokDTO);


        model.addAttribute("allKategori", allKategori);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        
        return "form-update-stok";
    }

    @PostMapping("stok/update/{idKatalog}")
    public String updateStokPost(@PathVariable("idKatalog") UUID idKatalog, @Valid @ModelAttribute UpdateStokRequestDTO stokDTO, Model model) {
        if (stokDTO.getStok() > 0) {
            stokDTO.setStatus(true);
        } else {
            stokDTO.setStatus(false);
        }
        var stokFromDto = stokMapper.updateStokRequestDTOToStok(stokDTO);
        var stok = stokService.updateStok(stokFromDto);

        model.addAttribute("idKatalog", stok.getIdKatalog());

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "success-update-stok";
    }
}

