package com.propenine.siku.repositorystok;
import com.propenine.siku.modelstok.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductDb extends JpaRepository<Product, UUID> { 
    List<Product> findAll();
    Optional<Product> findById(UUID idProduct);
    List<Product> findByKategoriId(Long kategoriId);
    List<Product> findByNamaProductContaining(String namaProduk);
    List<Product> findByKategoriIdAndNamaProductContaining(Long kategoriId, String productName);
}

