package com.propenine.siku.controller.katalog;

import com.propenine.siku.servicestok.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.propenine.siku.dto.katalog.KatalogMapper;
import com.propenine.siku.dto.katalog.KategoriMapper;
import com.propenine.siku.model.User;
import com.propenine.siku.model.katalog.Kategori;
import com.propenine.siku.modelstok.Product;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.katalog.KatalogService;
import com.propenine.siku.service.katalog.KategoriService;
import java.util.List;
import java.util.UUID;


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

        // Load all categories
        List<Kategori> allKategori = kategoriService.getAllKategori();
        model.addAttribute("allKategori", allKategori);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "katalog/viewall-katalog";
    }

    @GetMapping("/katalog/filter")
    public String filterByCategoryAndProductName(@RequestParam(value = "kategoriId", required = false) Long kategoriId,
            @RequestParam(value = "productName", required = false) String productName,
            Model model) {
        List<Product> filteredProducts;
    
        if (kategoriId != null && productName != null && !productName.isEmpty()) {
            filteredProducts = productService.getProductsByCategoryAndNameContaining(kategoriId, productName);
        } else if (kategoriId != null) {
            filteredProducts = productService.getProductsByCategory(kategoriId);
        } else if (productName != null && !productName.isEmpty()) {
            filteredProducts = productService.getProductsByNameContaining(productName);
        } else {
            filteredProducts = productService.getAllProduct();
        }
    
        model.addAttribute("listKatalog", filteredProducts);
    
        // Tambahkan semua kategori ke model
        List<Kategori> allKategori = kategoriService.getAllKategori();
        model.addAttribute("allKategori", allKategori);
    
        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
        }
    
        return "katalog/viewall-katalog";
    } 
}

