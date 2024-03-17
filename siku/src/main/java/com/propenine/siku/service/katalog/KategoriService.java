package com.propenine.siku.service.katalog;
import java.util.List;

import com.propenine.siku.model.katalog.Kategori;

public interface KategoriService {
    void saveKategori(Kategori kategori);
    List<Kategori> getAllKategori();
    Long countData();
    // void createKategori();
    Kategori getKategoriById(Long kategori);
}
