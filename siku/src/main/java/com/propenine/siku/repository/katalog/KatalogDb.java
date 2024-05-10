package com.propenine.siku.repository.katalog;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.propenine.siku.model.Klien;
import com.propenine.siku.model.katalog.Katalog;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional 
public interface KatalogDb extends JpaRepository<Katalog, UUID> {
} 
