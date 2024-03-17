package com.propenine.siku.repository;

import com.propenine.siku.model.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PesananRepository extends JpaRepository<Pesanan, Long> {

    List<Pesanan> findByNamaClient(String nama_client);
    List<Pesanan> findByNamaAgent(String nama_agent);
    List<Pesanan> findByStatusPesanan(String status_pesanan);
}
