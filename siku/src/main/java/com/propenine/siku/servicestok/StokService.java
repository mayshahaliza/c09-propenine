package com.propenine.siku.servicestok;
import com.propenine.siku.modelstok.Stok;
import com.propenine.siku.repositorystok.StokDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class StokService {
    @Autowired
    StokDb stokDb;
    
    public List<Stok> getAllStok() {
        return stokDb.findAll();
    }

    public Stok createStok(Stok stok) {
        return stokDb.save(stok);
    }

    public Stok updateStok(Stok stok) {
        return stokDb.save(stok);
    }

    public Stok getStokById(UUID idKatalog) {
        return stokDb.findById(idKatalog).get();
    }
}
