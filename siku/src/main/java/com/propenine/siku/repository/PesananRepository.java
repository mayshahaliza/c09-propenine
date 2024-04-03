package com.propenine.siku.repository;

import com.propenine.siku.model.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PesananRepository extends JpaRepository<Pesanan, Long> {
    
    //List<Pesanan> findByNamaClientContainingOrNamaAgentContaining(String namaClient, String namaAgent);

    // @Query("SELECT p FROM Pesanan p WHERE " +
    //        "(:searchInput IS NULL OR p.Klien LIKE %:searchInput% OR p.namaAgent LIKE %:searchInput%) AND " +
    //        "(:statusPesanan IS NULL OR :statusPesanan = '' OR p.statusPesanan = :statusPesanan) AND " +
    //        "(:tanggalPemesanan IS NULL OR :tanggalPemesanan = '' OR " +
    //        "(CASE WHEN :tanggalPemesanan = 'recent' THEN p.tanggalPemesanan >= :recentDate ELSE p.tanggalPemesanan <= :oldDate END))")
    // List<Pesanan> findByFilters(@Param("searchInput") String searchInput,
    //                             @Param("statusPesanan") String statusPesanan,
    //                             @Param("tanggalPemesanan") String tanggalPemesanan,
    //                             @Param("recentDate") LocalDate recentDate,
    //                             @Param("oldDate") LocalDate oldDate);

    @Query("SELECT p FROM Pesanan p WHERE " +
           "(:searchInput IS NULL OR p.klien.namaKlien LIKE %:searchInput% OR p.namaAgent LIKE %:searchInput%) AND " +
           "(:statusPesanan IS NULL OR :statusPesanan = '' OR p.statusPesanan = :statusPesanan) AND " +
           "(:tanggalPemesanan IS NULL OR :tanggalPemesanan = '' OR " +
           "(CASE WHEN :tanggalPemesanan = 'recent' THEN p.tanggalPemesanan >= :recentDate ELSE p.tanggalPemesanan <= :oldDate END))")
    List<Pesanan> findByFilters(@Param("searchInput") String searchInput,
                                @Param("statusPesanan") String statusPesanan,
                                @Param("tanggalPemesanan") String tanggalPemesanan,
                                @Param("recentDate") LocalDate recentDate,
                                @Param("oldDate") LocalDate oldDate);



    // List<Pesanan> findByNamaClient(String nama_client);
    List<Pesanan> findByNamaAgent(String nama_agent);
    List<Pesanan> findByStatusPesanan(String status_pesanan);
}
