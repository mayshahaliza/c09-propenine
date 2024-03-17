package com.propenine.siku.service.katalog;

import java.util.List;
import java.util.UUID;

import javax.xml.catalog.Catalog;

import com.propenine.siku.model.katalog.Katalog;

public interface KatalogService {
    void saveKatalog(Katalog katalog);
    List<Katalog> getAllkatalog();
    Katalog getKatalogById(UUID id);
}
