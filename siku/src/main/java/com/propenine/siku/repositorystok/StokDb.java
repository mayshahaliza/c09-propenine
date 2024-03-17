package com.propenine.siku.repositorystok;
import com.propenine.siku.modelstok.Stok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StokDb extends JpaRepository<Stok, UUID> { 
    List<Stok> findAll();
    Optional<Stok> findById(UUID idKatalog);
}
