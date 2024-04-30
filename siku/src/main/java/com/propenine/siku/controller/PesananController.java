package com.propenine.siku.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.propenine.siku.model.Klien;
import com.propenine.siku.model.Pesanan;
import com.propenine.siku.model.RekapAgent;
import com.propenine.siku.model.RekapKlien;
import com.propenine.siku.model.RekapPenjualan;
import com.propenine.siku.model.User;
import com.propenine.siku.modelstok.Product;
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

    // @GetMapping("/list")
    // public String listAllPesanan(
    // @RequestParam(name = "searchInput", required = false) String searchInput,
    // @RequestParam(name = "statusPesanan", required = false) String statusPesanan,
    // @RequestParam(name = "tanggalPemesanan", required = false) String
    // tanggalPemesanan,
    // Model model) {

    // List<Pesanan> pesananList;

    // if ((searchInput != null && !searchInput.isEmpty()) || (statusPesanan != null
    // && !statusPesanan.isEmpty())
    // || (tanggalPemesanan != null && !tanggalPemesanan.isEmpty())) {
    // pesananList = pesananService.findWithFilters(searchInput, statusPesanan,
    // tanggalPemesanan);
    // } else {
    // pesananList = pesananService.getAllPesanan();
    // }

    // pesananList.sort(Comparator.comparing(Pesanan::getStatusPesanan,
    // Comparator.comparing(status -> {
    // switch (status) {
    // case "Ongoing":
    // return 0;
    // case "Complete":
    // return 1;
    // case "Canceled":
    // return 2;
    // default:
    // return 3;
    // }
    // })));

    // model.addAttribute("pesananList", pesananList);

    // User loggedInUser = authenticationService.getLoggedInUser();
    // model.addAttribute("user", loggedInUser);

    // return "pesanan/list";
    // }

    @GetMapping("/list")
    public String listAllPesanan(
            @RequestParam(name = "searchInput", required = false) String searchInput,
            @RequestParam(name = "statusPesanan", required = false) String statusPesanan,
            @RequestParam(name = "tanggalPemesanan", required = false) String tanggalPemesanan,
            Model model) {

        List<Pesanan> pesananList;

        if ((searchInput != null && !searchInput.isEmpty()) || (statusPesanan != null
                && !statusPesanan.isEmpty())
                || (tanggalPemesanan != null && !tanggalPemesanan.isEmpty())) {
            pesananList = pesananService.findWithFilters(searchInput, statusPesanan,
                    tanggalPemesanan);
        } else {
            pesananList = pesananService.getAllPesanan();
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

        return "pesanan/create";
    }

    @PostMapping("/create")
    public String createPesanan(@ModelAttribute Pesanan pesanan, RedirectAttributes redirectAttributes) {
        Product product = pesanan.getProduct();
        Klien klien = pesanan.getKlien();
        User user = pesanan.getUser();
        int jumlahBarangPesanan = pesanan.getJumlahBarangPesanan();

        if (user != null && klien != null && product != null && jumlahBarangPesanan > 0) {
            int stokSaatIni = product.getStok();

            if (stokSaatIni >= jumlahBarangPesanan) {
                product.setStok(stokSaatIni - jumlahBarangPesanan);
                if (product.getStok() > 0) {
                    product.setStatus(true);
                } else {
                    product.setStatus(false);
                }
                pesanan.setJumlahBiayaPesanan(jumlahBarangPesanan * product.getHarga());
                productService.updateProduct(product);

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
        model.addAttribute("tanggalPemesanan", pesanan.getTanggalPemesanan().toString());
        model.addAttribute("tanggalSelesai", pesanan.getTanggalSelesai().toString());

        return "pesanan/update";
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

        if (updatedPesanan.getTanggalPemesanan() == null || updatedPesanan.getTanggalSelesai() == null) {
            updatedPesanan.setTanggalPemesanan(existingPesanan.getTanggalPemesanan());
            updatedPesanan.setTanggalSelesai(existingPesanan.getTanggalSelesai());
        }

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

        orderSummary.sort(Comparator.comparing(RekapPenjualan::getTotalQuantity).reversed());

        model.addAttribute("orderSummary", orderSummary);
        return "laporan-penjualan";
    }

    @GetMapping("/rekap-klien")
    public String getOrderSummaryByKlien(@RequestParam(required = false) Integer bulan,
            @RequestParam(required = false) Integer tahun,
            Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        List<RekapKlien> klienSummary;
        if (bulan != null && tahun != null) {
            klienSummary = pesananService.getKlienSummaryByMonthAndYear(bulan, tahun);
        } else {
            klienSummary = pesananService.getAllKlienSummaries();
        }

        if (klienSummary.isEmpty()) {
            model.addAttribute("message", "Tidak ada pesanan.");
            return "laporan-klien";
        }

        model.addAttribute("klienSummary", klienSummary);
        return "laporan-klien";
    }

    @GetMapping("/rekap-agent")
    public String getOrderSummaryByAgent(@RequestParam(required = false) Integer bulan,
            @RequestParam(required = false) Integer tahun,
            Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        List<RekapAgent> agentSummary;
        if (bulan != null && tahun != null) {
            agentSummary = pesananService.getAgentSummaryByMonthAndYear(bulan, tahun);
        } else {
            agentSummary = pesananService.getAllAgentSummaries();
        }

        if (agentSummary.isEmpty()) {
            model.addAttribute("message", "Tidak ada pesanan.");
            return "laporan-agent";
        }

        model.addAttribute("agentSummary", agentSummary);
        return "laporan-agent";
    }

    @GetMapping("/rekap-penjualan-chart")
    public String getOrderSummaryChart(@RequestParam(required = false) Integer bulan,
            @RequestParam(required = false) Integer tahun,
            Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        // Get the current month and year
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        // Fetch order summary data based on the provided month and year or use the
        // current month and year by default
        List<RekapPenjualan> orderSummary;
        if (bulan != null && tahun != null) {
            orderSummary = pesananService.getOrderSummaryByMonthAndYear(bulan, tahun);
        } else {
            orderSummary = pesananService.getOrderSummaryByMonthAndYear(currentMonth, currentYear);
        }

        orderSummary.sort(Comparator.comparing(RekapPenjualan::getTotalQuantity).reversed());

        List<RekapPenjualan> topProducts = orderSummary.stream()
                .limit(10)
                .collect(Collectors.toList());

        // Pass the order summary data and current month/year to the Thymeleaf template
        model.addAttribute("orderSummary", topProducts);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("currentYear", currentYear);

        return "rekap-penjualan-chart";
    }

    @GetMapping("/rekap-klien-chart")
    public String getKlienSummaryChart(@RequestParam(required = false) Integer bulan,
            @RequestParam(required = false) Integer tahun,
            Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        List<RekapKlien> klienSummary;
        if (bulan != null && tahun != null) {
            klienSummary = pesananService.getKlienSummaryByMonthAndYear(bulan, tahun);
        } else {
            klienSummary = pesananService.getKlienSummaryByMonthAndYear(currentMonth, currentYear);
        }

        model.addAttribute("klienSummary", klienSummary);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("currentYear", currentYear);

        return "rekap-klien-chart";
    }

    @GetMapping("/rekap-agent-chart")
    public String getAgentSummaryChart(@RequestParam(required = false) Integer bulan,
            @RequestParam(required = false) Integer tahun,
            Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        List<RekapAgent> agentSummary;
        if (bulan != null && tahun != null) {
            agentSummary = pesananService.getAgentSummaryByMonthAndYear(bulan, tahun);
        } else {
            agentSummary = pesananService.getAgentSummaryByMonthAndYear(currentMonth, currentYear);
        }

        // Pass the order summary data and current month/year to the Thymeleaf template
        model.addAttribute("agentSummary", agentSummary);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("currentYear", currentYear);

        // Return the name of the Thymeleaf template to render
        return "rekap-agent-chart";
    }

    @GetMapping("/rekap-penjualan-chart-data")
    @ResponseBody
    public List<RekapPenjualan> getOrderSummaryChartData(@RequestParam int bulan, @RequestParam int tahun,
            Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        // Fetch order summary data for the specified month and year
        List<RekapPenjualan> orderSummary = pesananService.getOrderSummaryByMonthAndYear(bulan, tahun);
        return orderSummary;
    }

    @GetMapping("/rekap-klien-chart-data")
    @ResponseBody
    public List<RekapKlien> getKlienSummaryChartData(@RequestParam int bulan, @RequestParam int tahun, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        // Fetch order summary data for the specified month and year
        List<RekapKlien> klienSummary = pesananService.getKlienSummaryByMonthAndYear(bulan, tahun);
        return klienSummary;
    }

    @GetMapping("/rekap-agent-chart-data")
    @ResponseBody
    public List<RekapAgent> getAgentSummaryChartData(@RequestParam int bulan, @RequestParam int tahun, Model model) {
        User loggedInUser = authenticationService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        // Fetch order summary data for the specified month and year
        List<RekapAgent> agentSummary = pesananService.getAgentSummaryByMonthAndYear(bulan, tahun);
        return agentSummary;
    }
}
