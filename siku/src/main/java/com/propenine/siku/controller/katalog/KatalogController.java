package com.propenine.siku.controller.katalog;

import com.propenine.siku.dtostok.ProductMapper;
import com.propenine.siku.servicestok.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.propenine.siku.dto.katalog.KatalogMapper;
import com.propenine.siku.dto.katalog.KategoriMapper;
import com.propenine.siku.dto.katalog.request.CreateKatalogRequestDTO;
import com.propenine.siku.model.User;
import com.propenine.siku.model.katalog.Katalog;
import com.propenine.siku.model.katalog.Kategori;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.katalog.KatalogService;
import com.propenine.siku.service.katalog.KategoriService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class KatalogController {
    @Autowired
    KategoriService kategoriService;

    @Autowired
    KatalogService katalogService;

    @Autowired
    KatalogMapper katalogMapper;

    @Autowired
    KategoriMapper kategoriMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    // CREATE KATALOG
    @GetMapping("katalog/tambah")
    public String formAddKatalog(Model model) {
        // Membuat DTO baru sebagai isian form pengguna
        var katalogDTO = new CreateKatalogRequestDTO();
        var allKategori = kategoriService.getAllKategori();
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        model.addAttribute("katalogDTO", katalogDTO);
        model.addAttribute("allKategori", allKategori);

        return "katalog/form-create-katalog";
    }

    @PostMapping("/katalog/tambah")
    public String addKatalog(@ModelAttribute("katalogDTO") @Valid CreateKatalogRequestDTO katalogDTO,
            @RequestParam("file") MultipartFile file, BindingResult bindingResult,
            Model model) {
        try {
            // Validate file name
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileName.contains("..")) {
                System.out.println("Not a valid file");
                return "failure"; // You can return an appropriate response here
            }

            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getAllErrors()
                        .stream()
                        .map(error -> {
                            if (error instanceof FieldError) {
                                FieldError fieldError = (FieldError) error;
                                return fieldError.getField() + ": " + error.getDefaultMessage();
                            }
                            return error.getDefaultMessage();
                        })
                        .collect(Collectors.toList());

                model.addAttribute("errors", errors);
                return "katalog/error-viewall"; // Mengarahkan ke halaman error-viewall jika terdapat kesalahan validasi
            }

            // Process the file and katalogDTO as needed
            var katalog = katalogMapper.createKatalogRequestDTOToKatalog(katalogDTO);
            katalog.setImage(Base64.getEncoder().encodeToString(file.getBytes()));

            // Save katalog to database
            katalogService.saveKatalog(katalog);
            User loggedInUser = authenticationService.getLoggedInUser();
            model.addAttribute("user", loggedInUser);

            model.addAttribute("id", katalog.getId());
            model.addAttribute("namaKatalog", katalogDTO.getNamaKatalog());

            return "katalog/success-create-katalog";
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    // VIEW DETAIL KATALOG NOT LOGIN
    // @GetMapping("/katalog/{id}")
    // public String detailKatalogNotLogin(@PathVariable("id") UUID id, Model model)
    // {
    // var katalog = katalogService.getKatalogById(id);
    //
    // model.addAttribute("katalog", katalog);
    // return "katalog/view-katalog-notlogin";
    // }

    // VIEW DETAIL KATALOG NOT LOGIN CONNECT TO PRODUCT
    @GetMapping("/katalog/{id}")
    public String detailKatalogNotLogin(@PathVariable("id") UUID id, Model model) {
        var katalog = productService.getProductById(id);

        model.addAttribute("katalog", katalog);
        return "katalog/view-katalog-notlogin";
    }


    // VIEW DETAIL KATALOG LOGIN CONNECT TO PRODUCT
    @GetMapping("/katalog/login/{id}")
    public String detailKatalogLogin(@PathVariable("id") UUID id, Model model) {
        var katalog = productService.getProductById(id);
        User loggedInUser = authenticationService.getLoggedInUser();

        model.addAttribute("user", loggedInUser);
        model.addAttribute("katalog", katalog);
        return "katalog/view-katalog-login";
    }

    // VIEWALL KATALOG CONNECT TO PRODUCT
    @GetMapping("katalog")
    public String listKatalog(Model model) {
        var listProduct = productService.getAllProduct();
        model.addAttribute("listKatalog", listProduct);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "katalog/viewall-katalog";
    }

    @GetMapping("/katalog/filter/{kategoriId}")
    public String filterByCategory(@PathVariable("kategoriId") Long kategoriId, Model model) {
        var filteredProducts = productService.getProductsByCategory(kategoriId);
        model.addAttribute("listKatalog", filteredProducts);

        var allKategori = kategoriService.getAllKategori();
        model.addAttribute("allKategori", allKategori);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "katalog/viewall-katalog";
    }

    // UPDATE KATALOG
    @GetMapping("/katalog/update/{id}")
    public String formUpdateKatalog(@PathVariable("id") UUID id, Model model) {
        var katalog = katalogService.getKatalogById(id);
        var katalogDTO = katalogMapper.katalogToUpdateKatalogRequestDTO(katalog);
        katalogDTO.setId(katalog.getId());

        var allKategori = kategoriService.getAllKategori();
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        model.addAttribute("allKategori", allKategori);
        model.addAttribute("katalogDTO", katalogDTO);
        model.addAttribute("katalog", katalog);

        return "katalog/form-update-katalog";

    }

    @PostMapping("/katalog/update/{id}")
    public String updateKatalog(@PathVariable("id") UUID id,
            @ModelAttribute("katalogDTO") @Valid CreateKatalogRequestDTO katalogDTO,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model) {
        try {
            // Retrieve existing katalog from database
            Katalog existingKatalog = katalogService.getKatalogById(id);
            if (existingKatalog == null) {
                return "Katalog not found";
            }

            // Update katalog details
            existingKatalog.setNamaKatalog(katalogDTO.getNamaKatalog());
            existingKatalog.setDeskripsi(katalogDTO.getDeskripsi());
            existingKatalog.setHarga(katalogDTO.getHarga());
            existingKatalog.setKategori(katalogDTO.getKategori());

            // Update image only if a new one is provided
            if (file != null && !file.isEmpty()) {
                // Validate file name
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                if (fileName.contains("..")) {
                    System.out.println("Not a valid file");
                    return "failure"; // You can return an appropriate response here
                }
                existingKatalog.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            }

            // Save updated katalog to database
            katalogService.saveKatalog(existingKatalog);
            User loggedInUser = authenticationService.getLoggedInUser();
            model.addAttribute("user", loggedInUser);

            model.addAttribute("id", existingKatalog.getId());
            model.addAttribute("namaKatalog", existingKatalog.getNamaKatalog());

            return "katalog/success-update-katalog";
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

}
