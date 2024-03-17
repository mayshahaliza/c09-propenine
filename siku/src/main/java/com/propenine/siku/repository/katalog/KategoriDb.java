package com.propenine.siku.repository.katalog;

import jakarta.transaction.Transactional;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.propenine.siku.model.katalog.Kategori;


public interface KategoriDb extends JpaRepository<Kategori, Long>  {
    
}
