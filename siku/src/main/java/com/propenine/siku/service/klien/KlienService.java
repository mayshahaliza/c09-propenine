package com.propenine.siku.service.klien;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.propenine.siku.model.Klien;
import com.propenine.siku.model.katalog.Katalog;
import com.propenine.siku.modelstok.Product;
import com.propenine.siku.repository.KlienDb;

@Service
public class KlienService {
    @Autowired
    KlienDb klienDb;

    public void saveKlien(Klien klien) {
        klienDb.save(klien);
    }

    public List<Klien> getAllKlien(){
        return klienDb.findAll();
    }

    public Klien getKlienById(UUID id) {
        return klienDb.findById(id).orElse(null);
    }

    public void deleteKlien(Klien klien) {
        klienDb.delete(klien);
    }

    public List<Klien> listKlienFiltered(String namaKlien, String rumahSakit) {
        List<Klien> klienFiltered;
        if (namaKlien != null && !namaKlien.isEmpty() && rumahSakit != null && !rumahSakit.isEmpty()) {
            // Mencari klien berdasarkan nama dan rumah sakit
            klienFiltered = klienDb.findByNamaKlienContainingIgnoreCaseAndRumahSakitContainingIgnoreCase(namaKlien, rumahSakit);
        } else if (namaKlien != null && !namaKlien.isEmpty()) {
            // Mencari klien berdasarkan nama saja
            klienFiltered = klienDb.findByNamaKlienContainingIgnoreCase(namaKlien);
        } else if (rumahSakit != null && !rumahSakit.isEmpty()) {
            // Mencari klien berdasarkan rumah sakit saja
            klienFiltered = klienDb.findByRumahSakitContainingIgnoreCase(rumahSakit);
        } else {
            // Jika tidak ada kriteria pencarian yang diberikan, maka tampilkan semua klien
            klienFiltered = klienDb.findAll();
        }

        return klienFiltered;
    }
}
