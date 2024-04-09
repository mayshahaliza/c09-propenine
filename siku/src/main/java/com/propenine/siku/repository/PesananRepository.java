package com.propenine.siku.repository;

import com.propenine.siku.model.Pesanan;
import com.propenine.siku.model.RekapPenjualan;
import com.propenine.siku.model.RekapKlien;
import com.propenine.siku.model.RekapAgent;
import com.propenine.siku.modelstok.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PesananRepository extends JpaRepository<Pesanan, Long> {

    // @Query("SELECT p FROM Pesanan p WHERE " +
    // "(:searchInput IS NULL OR p.klien.namaKlien LIKE %:searchInput% OR
    // p.namaAgent LIKE %:searchInput%) AND " +
    // "(:statusPesanan IS NULL OR :statusPesanan = '' OR p.statusPesanan =
    // :statusPesanan) AND " +
    // "(:tanggalPemesanan IS NULL OR :tanggalPemesanan = '' OR " +
    // "(CASE WHEN :tanggalPemesanan = 'recent' THEN p.tanggalPemesanan >=
    // :recentDate ELSE p.tanggalPemesanan <= :oldDate END))")
    // List<Pesanan> findByFilters(@Param("searchInput") String searchInput,
    // @Param("statusPesanan") String statusPesanan,
    // @Param("tanggalPemesanan") String tanggalPemesanan,
    // @Param("recentDate") LocalDate recentDate,
    // @Param("oldDate") LocalDate oldDate);

    @Query("SELECT p FROM Pesanan p WHERE " +
            "(:searchInput IS NULL OR p.klien.namaKlien LIKE %:searchInput% OR p.user.username LIKE %:searchInput%) AND "
            +
            "(:statusPesanan IS NULL OR :statusPesanan = '' OR p.statusPesanan = :statusPesanan) AND " +
            "(:tanggalPemesanan IS NULL OR :tanggalPemesanan = '' OR " +
            "(CASE WHEN :tanggalPemesanan = 'recent' THEN p.tanggalPemesanan >= :recentDate ELSE p.tanggalPemesanan <= :oldDate END))")
    List<Pesanan> findByFilters(@Param("searchInput") String searchInput,
            @Param("statusPesanan") String statusPesanan,
            @Param("tanggalPemesanan") String tanggalPemesanan,
            @Param("recentDate") LocalDate recentDate,
            @Param("oldDate") LocalDate oldDate);

    // List<Pesanan> findByNamaClient(String nama_client);
    // List<Pesanan> findByNamaAgent(String nama_agent);
    List<Pesanan> findByStatusPesanan(String status_pesanan);

    @Query("SELECT NEW com.propenine.siku.model.RekapPenjualan(p.product, SUM(p.jumlahBarangPesanan)) " +
            "FROM Pesanan p " +
            /*"WHERE p.statusPesanan = 'Complete' " + */
            "GROUP BY p.product")
    List<RekapPenjualan> getAllOrderSummaries();

    @Query("SELECT NEW com.propenine.siku.model.RekapPenjualan(p.product, CAST(SUM(p.jumlahBarangPesanan) AS java.lang.Long)) " +
            "FROM Pesanan p " +
            "WHERE EXTRACT(YEAR FROM p.tanggalPemesanan) = :tahun " +
            "AND EXTRACT(MONTH FROM p.tanggalPemesanan) = :bulan " +
            /*"AND p.statusPesanan = 'Complete' " + */
            "GROUP BY p.product")
    List<RekapPenjualan> getOrderSummaryByMonthAndYear(@Param("bulan") int bulan, @Param("tahun") int tahun);

    @Query("SELECT NEW com.propenine.siku.model.RekapKlien(p.klien, SUM(p.jumlahBarangPesanan)) " +
            "FROM Pesanan p " +
            /*"WHERE p.statusPesanan = 'Complete' " +*/
            "GROUP BY p.klien")
    List<RekapKlien> getAllKlienSummaries();

    @Query("SELECT NEW com.propenine.siku.model.RekapKlien(p.klien, CAST(SUM(p.jumlahBarangPesanan) AS java.lang.Long)) " +
            "FROM Pesanan p " +
            "WHERE EXTRACT(YEAR FROM p.tanggalPemesanan) = :tahun " +
            "AND EXTRACT(MONTH FROM p.tanggalPemesanan) = :bulan " +
            /*"AND p.statusPesanan = 'Complete' " +*/
            "GROUP BY p.klien")
    List<RekapKlien> getKlienSummaryByMonthAndYear(@Param("bulan") int bulan, @Param("tahun") int tahun);

    @Query("SELECT NEW com.propenine.siku.model.RekapAgent(p.user, SUM(p.jumlahBarangPesanan)) " +
            "FROM Pesanan p " +
            /*"WHERE p.statusPesanan = 'Complete' " +*/
            "GROUP BY p.user")
    List<RekapAgent> getAllAgentSummaries();

    @Query("SELECT NEW com.propenine.siku.model.RekapAgent(p.user, CAST(SUM(p.jumlahBarangPesanan) AS java.lang.Long)) " +
            "FROM Pesanan p " +
            "WHERE EXTRACT(YEAR FROM p.tanggalPemesanan) = :tahun " +
            "AND EXTRACT(MONTH FROM p.tanggalPemesanan) = :bulan " +
            /*"AND p.statusPesanan = 'Complete' " +*/
            "GROUP BY p.user")
    List<RekapAgent> getAgentSummaryByMonthAndYear(@Param("bulan") int bulan, @Param("tahun") int tahun);
}
