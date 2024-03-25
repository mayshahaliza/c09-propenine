package com.propenine.siku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.propenine.siku.model.Pesanan;
import com.propenine.siku.modelstok.Product;
import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.PesananService;
import com.propenine.siku.servicestok.ProductService;

@Controller
@RequestMapping("/pesanan")
public class PesananController {
    
    private final ProductService productService;
    private final PesananService pesananService;
    
    @Autowired
    private AuthenticationService authenticationService;


    @Autowired
    public PesananController(PesananService pesananService, ProductService productService) {
        this.pesananService = pesananService;
        this.productService = productService;
    }


    @GetMapping("/")
    public String root() {
        return "redirect:/pesanan/list";
    }

    
    // @GetMapping("/list")
    // public String listAllPesanan(
    //         @RequestParam(name = "namaClient", required = false) String namaClient,
    //         @RequestParam(name = "namaAgent", required = false) String namaAgent,
    //         @RequestParam(name = "statusPesanan", required = false) String statusPesanan,
    //         Model model) {

        


    //     User loggedInUser = authenticationService.getLoggedInUser();
    //     model.addAttribute("user", loggedInUser);
    //     return "pesanan/list"; // HTML template for listing pesanans
    // }

    @GetMapping("/list")
    public String listAllPesanan(
            @RequestParam(name = "namaClient", required = false) String namaClient,
            @RequestParam(name = "namaAgent", required = false) String namaAgent,
            @RequestParam(name = "statusPesanan", required = false) String statusPesanan,
            Model model) {

        // Misalnya, Anda memiliki sebuah layanan pesanan yang dapat mengambil daftar pesanan
        List<Pesanan> pesananList = pesananService.getAllPesanan();

        // Menyimpan daftar pesanan dalam model untuk ditampilkan di halaman HTML
        model.addAttribute("pesananList", pesananList);

        // Mengambil informasi user yang sedang login
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "pesanan/list"; // HTML template for listing pesanans
    }


    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        List<Product> productList = productService.getAllProduct(); // Assuming you have a service for Stok
        model.addAttribute("pesanan", new Pesanan());
        model.addAttribute("productList", productList);
        return "pesanan/create"; // HTML template for creating a new pesanan
    }
    
    @PostMapping("/create")
    public String createPesanan(@ModelAttribute Pesanan pesanan, RedirectAttributes redirectAttributes) {
        Product product = pesanan.getProduct();
        int jumlahBarangPesanan = pesanan.getJumlahBarangPesanan();
        if (product != null && jumlahBarangPesanan > 0) {
            int stokSaatIni = product.getStok();
            if (stokSaatIni >= jumlahBarangPesanan) {
                product.setStok(stokSaatIni - jumlahBarangPesanan);
                productService.updateProduct(product); // Update stok di database
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", "Stok tidak mencukupi");
                return "redirect:/pesanan/create";
            }
        }
        pesananService.createPesanan(pesanan);
        return "redirect:/pesanan/list";
    }


    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
      List<Product> productList = productService.getAllProduct(); // Assuming you have a service for Stok
        model.addAttribute("productList", productList);
        Pesanan pesanan = pesananService.getPesananById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pesanan Id:" + id));
        model.addAttribute("pesanan", pesanan);
        return "pesanan/update"; // HTML template for updating an existing pesanana
    }

    @PostMapping("/update/{id}")
    public String updatePesanan(@PathVariable Long id, @ModelAttribute Pesanan updatedPesanan, RedirectAttributes redirectAttributes) {
        Pesanan existingPesanan = pesananService.getPesananById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pesanan Id:" + id));
        Product product = updatedPesanan.getProduct();
        int jumlahBarangPesanan = updatedPesanan.getJumlahBarangPesanan();
        int jumlahBarangPesananSebelumnya = existingPesanan.getJumlahBarangPesanan();
        int selisihJumlahPesanan = jumlahBarangPesananSebelumnya - jumlahBarangPesanan;
        int hasilAkhir = product.getStok() + selisihJumlahPesanan;
        if (hasilAkhir >= 0) {
            if (product != null && jumlahBarangPesanan > 0) {
                product.setStok(product.getStok() + selisihJumlahPesanan);
                productService.updateProduct(product);
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", "Jumlah barang tidak boleh 0");
                return "redirect:/pesanan/update/" + id;
            }
        }
        else {
            redirectAttributes.addFlashAttribute("warningMessage", "Stok tidak mencukupi");
            return "redirect:/pesanan/update/" + id;
        }

        pesananService.updatePesanan(id, updatedPesanan);
        return "redirect:/pesanan/list";
    }
    

    





  

}
