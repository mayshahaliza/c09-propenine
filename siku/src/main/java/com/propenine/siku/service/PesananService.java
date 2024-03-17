package com.propenine.siku.service;

import com.propenine.siku.model.Pesanan;
import com.propenine.siku.repository.PesananRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PesananService {

    private final PesananRepository pesananRepository;

    @Autowired
    public PesananService(PesananRepository pesananRepository) {
        this.pesananRepository = pesananRepository;
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

    public List<Pesanan> getPesananByNamaClient(String namaClient) {
        return pesananRepository.findByNamaClient(namaClient);
    }

    public List<Pesanan> getPesananByNamaAgent(String namaAgent) {
        return pesananRepository.findByNamaAgent(namaAgent);
    }

    public List<Pesanan> getPesananByStatusPesanan(String statusPesanan) {
        return pesananRepository.findByStatusPesanan(statusPesanan);
    }

    public Pesanan updatePesanan(Long id, Pesanan updatedPesanan) {
        return pesananRepository.findById(id)
                .map(pesanan -> {
                    pesanan.setNamaClient(updatedPesanan.getNamaClient());
                    pesanan.setNamaAgent(updatedPesanan.getNamaAgent());
                    pesanan.setStatusPesanan(updatedPesanan.getStatusPesanan());
                    pesanan.setJumlahBarangPesanan(updatedPesanan.getJumlahBarangPesanan()); // Update jumlah pesanan
    
                    return pesananRepository.save(pesanan);
                })
                .orElseGet(() -> {
                    updatedPesanan.setId(id);
                    return pesananRepository.save(updatedPesanan);
                });
    }
    

    
}
