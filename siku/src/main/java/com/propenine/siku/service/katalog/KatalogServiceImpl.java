package com.propenine.siku.service.katalog;

import java.util.List;
import java.util.UUID;

import javax.xml.catalog.Catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.propenine.siku.model.Klien;
import com.propenine.siku.model.katalog.Katalog;
import com.propenine.siku.repository.katalog.KatalogDb;

@Service
public class KatalogServiceImpl implements KatalogService {
    @Autowired
    KatalogDb katalogDb;

    @Override
    public void saveKatalog(Katalog katalog) {
        katalogDb.save(katalog);
    }

    @Override
    public List<Katalog> getAllkatalog() {
        return katalogDb.findAll();
    }

    @Override
    public Katalog getKatalogById(UUID id) {
        return katalogDb.findById(id).orElse(null);
    }
}
    
    

