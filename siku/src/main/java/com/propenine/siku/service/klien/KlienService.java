package com.propenine.siku.service.klien;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.propenine.siku.model.Klien;
import com.propenine.siku.model.katalog.Katalog;
import com.propenine.siku.modelstok.Stok;
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

    public List<Klien> listKlienFiltered(String namaKlien){
        List<Klien> klienFiltered = klienDb.findByNamaKlienContainingIgnoreCase(namaKlien);
        return klienFiltered;
    }
}
