package com.propenine.siku.controllerstok;

import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.katalog.KategoriService;
import com.propenine.siku.servicestok.ProductService;
import org.springframework.util.StringUtils;
import jakarta.validation.Valid;

import com.propenine.siku.dtostok.request.CreateProductRequestDTO;
import com.propenine.siku.dtostok.request.UpdateProductRequestDTO;
import com.propenine.siku.modelstok.Product;
import com.propenine.siku.dtostok.ProductMapper;
import com.propenine.siku.repositorystok.ProductDb;
import java.util.UUID;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.parser.MediaType;
//import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProductController {
    @Autowired
    KategoriService kategoriService;

    @Autowired
    private ResourceLoader resourceLoader;

    // Autentikasi
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("stok")
    public String listProduct(Model model) {
        var listProduct = productService.getAllProduct();
        model.addAttribute("listProduct", listProduct);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "viewall-product";
    }

    @GetMapping("product/tambah")
    public String formAddProduct(Model model) {
        System.out.println("ke sini");
        var productDTO = new CreateProductRequestDTO();
        var allKategori = kategoriService.getAllKategori();
        model.addAttribute("allKategori", allKategori);
        model.addAttribute("productDTO", productDTO);

        // Autentikasi
        User loggedInUser = authenticationService.getLoggedInUser();
        System.out.println(loggedInUser.toString());
        System.out.println(loggedInUser.getRole());
        model.addAttribute("user", loggedInUser);

        return "form-tambah-product";
    }

    @PostMapping("/product/tambah")
    public String addProduct(@Valid @ModelAttribute("productDTO") CreateProductRequestDTO productDTO,
            @RequestParam("imageFile") MultipartFile imageFile,
            BindingResult bindingResult, Model model) {

        if (imageFile.isEmpty()) {
            return "Image file is empty";
        }

        try {
            byte[] bytes = imageFile.getBytes();
            String name = System.currentTimeMillis() + imageFile.getOriginalFilename();
            Path path = Paths.get(uploadDir).resolve(name); // Resolve path within uploadDir
            Files.createDirectories(path.getParent()); // Create parent directories if they don't exist
            Files.write(path, bytes);

            if (productDTO.getStok() > 0) {
                productDTO.setStatus(true);
            } else {
                productDTO.setStatus(false);
            }
            productDTO.setImage(name);
            Product product = productMapper.createProductRequestDTOToProduct(productDTO);
            productService.createProduct(product);
            System.out.println("berhasil menambahkan product");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IOException properly, such as logging or informing the user
        }

        return "form-tambah-product";
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
    public String updateProductPost(@PathVariable("idProduct") UUID idProduct,
            @Valid @ModelAttribute("productDTO") UpdateProductRequestDTO productDTO, Model model,
            @RequestParam(required = false) MultipartFile imageFile) {
        if (productDTO.getStok() > 0) {
            productDTO.setStatus(true);
        } else {
            productDTO.setStatus(false);
        }
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] bytes = imageFile.getBytes();
                String name = System.currentTimeMillis() + imageFile.getOriginalFilename();
                Path path = Paths.get(uploadDir).resolve(name); // Resolve path within uploadDir
                Files.createDirectories(path.getParent()); // Create parent directories if they don't exist
                Files.write(path, bytes);

                if (productDTO.getStok() > 0) {
                    productDTO.setStatus(true);
                } else {
                    productDTO.setStatus(false);
                }
                System.out.println("berhasil menambahkan product");
                productDTO.setImage(name);
            } else {
                Product existingProduct = productService.getProductById(idProduct);
                productDTO.setImage(existingProduct.getImage());
            }
            var productFromDto = productMapper.updateProductRequestDTOToProduct(productDTO);
            var product = productService.updateProduct(productFromDto);
            model.addAttribute("idProduct", product.getIdProduct());
            model.addAttribute("productUpdated", true);
            System.out.println("success update product");
            // Autentikasi
            User loggedInUser = authenticationService.getLoggedInUser();
            model.addAttribute("user", loggedInUser);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "form-update-product";
    }

    public String upload(MultipartFile multipartFile, String categoryImage, String pathFile) {
        if (multipartFile != null) {
            if (multipartFile.getSize() == 0) {
                throw new IllegalArgumentException("Please pick an image");
            }
            try {
                String cleanName = cleanName(categoryImage, Objects.requireNonNull(multipartFile.getContentType()));
                Path uploadPath = Paths.get("../resources/main/" + pathFile).toAbsolutePath().normalize()
                        .resolve(cleanName);

                System.out.println(uploadPath.toAbsolutePath());
                System.out.println(uploadPath);
                Files.createDirectories(uploadPath.getParent()); // Create directories if they don't exist
                multipartFile.transferTo(uploadPath);
                return cleanName;
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Failed to upload image: " + e.getMessage());
            }
        }
        throw new IllegalArgumentException("Image is empty");
    }

    public String cleanName(String category, String type) {
        String[] splitType = type.split("/");
        return System.currentTimeMillis() + category + "." + splitType[1];
    }

    @GetMapping("/product-images/{filename}")
    @ResponseBody
    public byte[] serveFile(@PathVariable String filename) throws IOException {
        Path file = Paths.get(uploadDir).resolve(filename);
        return Files.readAllBytes(file);
    }

}
