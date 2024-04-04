package com.propenine.siku.controllerstok;
import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.katalog.KategoriService;
import com.propenine.siku.servicestok.ProductService;

import jakarta.validation.Valid;

import com.propenine.siku.dtostok.request.CreateProductRequestDTO;
import com.propenine.siku.dtostok.request.UpdateProductRequestDTO;
import com.propenine.siku.modelstok.Product;
import com.propenine.siku.dtostok.ProductMapper;
import com.propenine.siku.repositorystok.ProductDb;
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
public class ProductController {
    @Autowired
    KategoriService kategoriService;

    // Autentikasi
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("stok")
    public String listGudang(Model model){
        var listProduct = productService.getAllProduct();
        model.addAttribute("listProduct", listProduct);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "viewall-product";
    }
    
    @GetMapping("product/tambah")
    public String formAddProduct(Model model){
        var productDTO = new CreateProductRequestDTO();
        var allKategori = kategoriService.getAllKategori();
        model.addAttribute("allKategori", allKategori);
        model.addAttribute("productDTO", productDTO);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "form-tambah-product";
    }

    @PostMapping("product/tambah")
    public String addProduct(@Valid @ModelAttribute CreateProductRequestDTO productDTO, Model model){
        if (productDTO.getStok() > 0) {
            productDTO.setStatus(true);
        } else {
            productDTO.setStatus(false);
        }
        var product = productMapper.createProductRequestDTOToProduct(productDTO);

        productService.createProduct(product);

        model.addAttribute("product", productDTO);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "success-create-product";
    }

    @GetMapping("product/update/{idProduct}")
    public String formUpdateProduct(@PathVariable("idProduct") UUID idProduct, Model model) {
        var product = productService.getProductById(idProduct);
        var productDTO = productMapper.productToUpdateProductRequestDTO(product);
        var allKategori = kategoriService.getAllKategori();

        model.addAttribute("productDTO", productDTO);


        model.addAttribute("allKategori", allKategori);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        
        return "form-update-product";
    }

    @PostMapping("product/update/{idProduct}")
    public String updateProductPost(@PathVariable("idProduct") UUID idProduct, @Valid @ModelAttribute UpdateProductRequestDTO productDTO, Model model) {
        if (productDTO.getStok() > 0) {
            productDTO.setStatus(true);
        } else {
            productDTO.setStatus(false);
        }
        var productFromDto = productMapper.updateProductRequestDTOToProduct(productDTO);
        var product = productService.updateProduct(productFromDto);

        model.addAttribute("idProduct", product.getIdProduct());

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "success-update-product";
    }
}

