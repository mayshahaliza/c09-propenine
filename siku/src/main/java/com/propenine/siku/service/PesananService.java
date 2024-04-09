package com.propenine.siku.service;

import com.propenine.siku.model.Pesanan;
import com.propenine.siku.modelstok.Product;
import com.propenine.siku.repository.PesananRepository;
import com.propenine.siku.servicestok.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PesananService {

    private final PesananRepository pesananRepository;
    private final ProductService productService;

    @Autowired
    public PesananService(PesananRepository pesananRepository, ProductService productService) {
        this.pesananRepository = pesananRepository;
        this.productService = productService;
    }

    public Pesanan createPesanan(Pesanan pesanan) {
        return pesananRepository.save(pesanan);
    }

    public List<Pesanan> getAllPesanan() {
        return pesananRepository.findAll();
    }

    public Optional<Pesanan> getPesananById(Long id) {
        return pesananRepository.findById(id);
    }

    public List<Pesanan> getPesananByStatusPesanan(String statusPesanan) {
        return pesananRepository.findByStatusPesanan(statusPesanan);
    }

    public Pesanan updatePesanan(Long id, Pesanan updatedPesanan) {
        return pesananRepository.findById(id)
                .map(pesanan -> {
                    pesanan.setStatusPesanan(updatedPesanan.getStatusPesanan());
                    pesanan.setJumlahBarangPesanan(updatedPesanan.getJumlahBarangPesanan());
                    pesanan.setKlien(updatedPesanan.getKlien());
                    pesanan.setUser(updatedPesanan.getUser());
                    pesanan.setJumlahBarangPesanan(updatedPesanan.getJumlahBarangPesanan());
                    pesanan.setTanggalPemesanan(updatedPesanan.getTanggalPemesanan());
                    pesanan.setTanggalSelesai(updatedPesanan.getTanggalSelesai());
                    return pesananRepository.save(pesanan);
                })
                .orElseGet(() -> {
                    updatedPesanan.setId(id);
                    return pesananRepository.save(updatedPesanan);
                });
    }


    public List<Pesanan> findWithFilters(String searchInput, String statusPesanan, String tanggalPemesanan) {
        LocalDate recentDate = LocalDate.now().minus(14, ChronoUnit.DAYS);
        LocalDate oldDate = LocalDate.now().minus(28, ChronoUnit.DAYS); // Adjust according to your requirement

        return pesananRepository.findByFilters(searchInput, statusPesanan,
                tanggalPemesanan, recentDate, oldDate);
    }

    @Transactional
    public void deletePesanan(Long id) {
        Optional<Pesanan> optionalPesanan = pesananRepository.findById(id);
        if (optionalPesanan.isPresent()) {
            Pesanan pesanan = optionalPesanan.get();
            Product product = pesanan.getProduct();
            int jumlahBarangPesanan = pesanan.getJumlahBarangPesanan();
            int stokSaatIni = product.getStok();
            product.setStok(stokSaatIni + jumlahBarangPesanan);
            pesananRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Pesanan dengan ID " + id + " tidak ditemukan");
        }
    }

}
