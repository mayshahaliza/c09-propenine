package com.propenine.siku.service.katalog;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.propenine.siku.dto.katalog.KategoriMapper;
import com.propenine.siku.dto.katalog.request.CreateKategoriRequestDTO;
import com.propenine.siku.model.katalog.Kategori;
import com.propenine.siku.repository.katalog.KategoriDb;

@Service
public class KategoriServiceImpl implements KategoriService{
    @Autowired
    private KategoriDb kategoriDb;

    @Autowired
    private KategoriMapper kategoriMapper;

    @Override
    public void saveKategori(Kategori kategori) {
        kategoriDb.save(kategori);
    }

    @Override
    public List<Kategori> getAllKategori() {
        return kategoriDb.findAll();
    }

    @Override
    public Long countData(){
        return kategoriDb.count();
    }
    
    // @Override
    // public void createKategori(){
    //     String[] manualKategori = {
    //         "Surgery",
    //         "Urologi",
    //         "Anesthesia",
    //         "Obstetrics & Gynecology"};

    //         if (kategoriDb.count() == 0) {
    //             for (String namaKategori : manualKategori) {
    //                 var kategoriDTO = new CreateKategoriRequestDTO();
    //                 kategoriDTO.setNamaKategori(namaKategori);

    //                 var kategori = kategoriMapper.createKategoriRequestDTOToKategori(kategoriDTO);

    //                 saveKategori(kategori);
    //             }
    //         }
    // }

    public Kategori getKategoriById(Long kategoriId) {
        Optional<Kategori> kategoriOptional = kategoriDb.findById(kategoriId);
        return kategoriOptional.orElse(null);
    }
    
}
