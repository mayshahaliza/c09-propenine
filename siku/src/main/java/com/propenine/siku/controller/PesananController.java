package com.propenine.siku.controller;

import java.util.List;

import com.propenine.siku.model.RekapPenjualan;
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

import com.propenine.siku.model.Klien;
import com.propenine.siku.model.Pesanan;
import com.propenine.siku.modelstok.Product;
import com.propenine.siku.model.User;
import com.propenine.siku.service.AuthenticationService;
import com.propenine.siku.service.PesananService;
import com.propenine.siku.service.UserServiceImpl;
import com.propenine.siku.service.klien.KlienService;
import com.propenine.siku.servicestok.ProductService;

@Controller
@RequestMapping("/pesanan")
public class PesananController {

    private final ProductService productService;
    private final PesananService pesananService;
    private final KlienService klienService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    public PesananController(PesananService pesananService, ProductService productService, KlienService klienService,
            UserServiceImpl userServiceImpl) {
        this.pesananService = pesananService;
        this.productService = productService;
        this.klienService = klienService;
        this.userServiceImpl = userServiceImpl;

    }

    @GetMapping("/")
    public String root() {
        return "redirect:/pesanan/list";
    }

    @GetMapping("/list")
    public String listAllPesanan(
            @RequestParam(name = "searchInput", required = false) String searchInput,
            @RequestParam(name = "statusPesanan", required = false) String statusPesanan,
            @RequestParam(name = "tanggalPemesanan", required = false) String tanggalPemesanan,
            Model model) {

        List<Pesanan> pesananList;

        if ((searchInput != null && !searchInput.isEmpty()) || (statusPesanan != null && !statusPesanan.isEmpty())
                || (tanggalPemesanan != null && !tanggalPemesanan.isEmpty())) {
            pesananList = pesananService.findWithFilters(searchInput, statusPesanan, tanggalPemesanan);
        } else {
            pesananList = pesananService.getAllPesanan();
        }

        model.addAttribute("pesananList", pesananList);

        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        return "pesanan/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        List<Product> productList = productService.getAllProduct();
        List<Klien> klienList = klienService.getAllKlien();
        List<User> userList = userServiceImpl.getAllUsers();
        model.addAttribute("pesanan", new Pesanan());
        model.addAttribute("productList", productList);
        model.addAttribute("klienList", klienList);
        model.addAttribute("userList", userList);

        return "pesanan/create"; // HTML template for creating a new pesanan
    }

    // @PostMapping("/create")
    // public String createPesanan(@ModelAttribute Pesanan pesanan,
    // RedirectAttributes redirectAttributes) {
    // Product product = pesanan.getProduct();
    // Klien klien = pesanan.getKlien();
    // User user = pesanan.getUser();
    // int jumlahBarangPesanan = pesanan.getJumlahBarangPesanan();
    // if (user != null && klien != null && product != null && jumlahBarangPesanan >
    // 0) {
    // int stokSaatIni = product.getStok();
    // if (stokSaatIni >= jumlahBarangPesanan) {
    // product.setStok(stokSaatIni - jumlahBarangPesanan);
    // productService.updateProduct(product); // Update stok di database
    // } else {
    // redirectAttributes.addFlashAttribute("warningMessage", "Stok tidak
    // mencukupi");
    // return "redirect:/pesanan/create";
    // }
    // }
    // pesananService.createPesanan(pesanan);
    // return "redirect:/pesanan/list";
    // }
    @PostMapping("/create")
    public String createPesanan(@ModelAttribute Pesanan pesanan, RedirectAttributes redirectAttributes) {
        Product product = pesanan.getProduct();
        Klien klien = pesanan.getKlien();
        User user = pesanan.getUser();
        int jumlahBarangPesanan = pesanan.getJumlahBarangPesanan();

        // Pastikan semua nilai yang diperlukan telah diisi dan valid
        if (user != null && klien != null && product != null && jumlahBarangPesanan > 0) {
            int stokSaatIni = product.getStok();

            // Pastikan stok produk mencukupi
            if (stokSaatIni >= jumlahBarangPesanan) {
                product.setStok(stokSaatIni - jumlahBarangPesanan);
                productService.updateProduct(product); // Update stok di database

                // Simpan pesanan ke dalam database
                pesananService.createPesanan(pesanan);

                return "redirect:/pesanan/list";
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", "Stok tidak mencukupi");
            }
        } else {
            redirectAttributes.addFlashAttribute("warningMessage", "Harap lengkapi semua data pesanan");
        }

        return "redirect:/pesanan/create";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        List<Product> productList = productService.getAllProduct();
        List<Klien> klienList = klienService.getAllKlien();
        List<User> userList = userServiceImpl.getAllUsers();

        model.addAttribute("userList", userList);
        model.addAttribute("klienList", klienList);
        model.addAttribute("productList", productList);
        Pesanan pesanan = pesananService.getPesananById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pesanan Id:" + id));
        model.addAttribute("pesanan", pesanan);
        return "pesanan/update"; // HTML template for updating an existing pesanana
    }

    @PostMapping("/update/{id}")
    public String updatePesanan(@PathVariable Long id, @ModelAttribute Pesanan updatedPesanan,
            RedirectAttributes redirectAttributes) {
        Pesanan existingPesanan = pesananService.getPesananById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pesanan Id:" + id));
        Product product = updatedPesanan.getProduct();
        Klien klien = updatedPesanan.getKlien();
        User user = updatedPesanan.getUser();
        int jumlahBarangPesanan = updatedPesanan.getJumlahBarangPesanan();
        int jumlahBarangPesananSebelumnya = existingPesanan.getJumlahBarangPesanan();
        int selisihJumlahPesanan = jumlahBarangPesananSebelumnya - jumlahBarangPesanan;
        int hasilAkhir = product.getStok() + selisihJumlahPesanan;
        if (hasilAkhir >= 0) {
            if (user != null && klien != null && product != null && jumlahBarangPesanan > 0) {
                product.setStok(product.getStok() + selisihJumlahPesanan);
                productService.updateProduct(product);
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", "Jumlah barang tidak boleh 0");
                return "redirect:/pesanan/update/" + id;
            }
        } else {
            redirectAttributes.addFlashAttribute("warningMessage", "Stok tidak mencukupi");
            return "redirect:/pesanan/update/" + id;
        }

        pesananService.updatePesanan(id, updatedPesanan);
        return "redirect:/pesanan/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePesanan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pesananService.deletePesanan(id);
            redirectAttributes.addFlashAttribute("successMessage", "Pesanan has been deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error occurred while deleting the pesanan.");
        }
        return "redirect:/pesanan/list";
    }

    @GetMapping("/rekap-penjualan")
    public String getOrderSummary(@RequestParam(required = false) Integer bulan,
                                  @RequestParam(required = false) Integer tahun,
                                  Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        List<RekapPenjualan> orderSummary;
        if (bulan != null && tahun != null) {
            orderSummary = pesananService.getOrderSummaryByMonthAndYear(bulan, tahun);
        } else {
            orderSummary = pesananService.getAllOrderSummaries();
        }

        if (orderSummary.isEmpty()) {
            model.addAttribute("message", "Tidak ada pesanan.");
            return "laporan-penjualan";
        }

        model.addAttribute("orderSummary", orderSummary);
        return "laporan-penjualan";
    }


}
