package com.propenine.siku.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.propenine.siku.model.Klien;

import jakarta.transaction.Transactional;

@Repository
@Transactional 
public interface KlienDb extends JpaRepository<Klien, UUID> {
    List<Klien> findByNamaKlienContainingIgnoreCase(String namaKlien);
    List<Klien> findByNamaKlienContainingIgnoreCaseAndRumahSakitContainingIgnoreCase(String namaKlien, String rumahSakit);
    List<Klien> findByRumahSakitContainingIgnoreCase(String rumahSakit); 
}
