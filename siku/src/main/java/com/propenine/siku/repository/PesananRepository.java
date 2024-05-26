package com.propenine.siku.repository;

import com.propenine.siku.model.Klien;
import com.propenine.siku.model.Pesanan;
import com.propenine.siku.model.RekapPenjualan;
import com.propenine.siku.model.User;
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

        @Query("SELECT p FROM Pesanan p WHERE " +
                        "(:searchInput IS NULL OR LOWER(CONCAT(p.klien.namaKlien, ' ', p.user.username)) LIKE LOWER(CONCAT('%', :searchInput, '%'))) AND "
                        +
                        "(:statusPesanan IS NULL OR :statusPesanan = '' OR p.statusPesanan = :statusPesanan) AND " +
                        "(:tanggalPemesanan IS NULL OR :tanggalPemesanan = '' OR " +
                        "(CASE WHEN :tanggalPemesanan = 'recent' THEN p.tanggalPemesanan >= :recentDate ELSE p.tanggalPemesanan <= :oldDate END))")
        List<Pesanan> findByFilters(@Param("searchInput") String searchInput,
                        @Param("statusPesanan") String statusPesanan,
                        @Param("tanggalPemesanan") String tanggalPemesanan,
                        @Param("recentDate") LocalDate recentDate,
                        @Param("oldDate") LocalDate oldDate);

        List<Pesanan> findByStatusPesanan(String status_pesanan);

        @Query("SELECT NEW com.propenine.siku.model.RekapPenjualan(p.product, SUM(p.jumlahBarangPesanan)) " +
                        "FROM Pesanan p " +
                        "WHERE p.statusPesanan = 'Complete' " +
                        "GROUP BY p.product")
        List<RekapPenjualan> getAllOrderSummaries();

        @Query("SELECT NEW com.propenine.siku.model.RekapPenjualan(p.product, CAST(SUM(p.jumlahBarangPesanan) AS java.lang.Long)) "
                        +
                        "FROM Pesanan p " +
                        "WHERE EXTRACT(YEAR FROM p.tanggalPemesanan) = :tahun " +
                        "AND EXTRACT(MONTH FROM p.tanggalPemesanan) = :bulan " +
                        "AND p.statusPesanan = 'Complete' " +
                        "GROUP BY p.product")
        List<RekapPenjualan> getOrderSummaryByMonthAndYear(@Param("bulan") int bulan, @Param("tahun") int tahun);

        @Query("SELECT NEW com.propenine.siku.model.RekapKlien(p.klien, SUM(p.jumlahBarangPesanan), SUM(p.jumlahBiayaPesanan)) "
                        +
                        "FROM Pesanan p " +
                        "WHERE p.statusPesanan = 'Complete' " +
                        "GROUP BY p.klien")
        List<RekapKlien> getAllKlienSummaries();

        @Query("SELECT NEW com.propenine.siku.model.RekapKlien(p.klien, CAST(SUM(p.jumlahBarangPesanan) AS java.lang.Long), CAST(SUM(p.jumlahBiayaPesanan) AS java.lang.Long)) "
                        +
                        "FROM Pesanan p " +
                        "WHERE EXTRACT(YEAR FROM p.tanggalPemesanan) = :tahun " +
                        "AND EXTRACT(MONTH FROM p.tanggalPemesanan) = :bulan " +
                        "AND p.statusPesanan = 'Complete' " +
                        "GROUP BY p.klien")
        List<RekapKlien> getKlienSummaryByMonthAndYear(@Param("bulan") int bulan, @Param("tahun") int tahun);

        @Query("SELECT NEW com.propenine.siku.model.RekapAgent(p.user, SUM(p.jumlahBarangPesanan), SUM(p.jumlahBiayaPesanan)) "
                        +
                        "FROM Pesanan p " +
                        "WHERE p.statusPesanan = 'Complete' " +
                        "GROUP BY p.user")
        List<RekapAgent> getAllAgentSummaries();

        @Query("SELECT NEW com.propenine.siku.model.RekapAgent(p.user, CAST(SUM(p.jumlahBarangPesanan) AS java.lang.Long), CAST(SUM(p.jumlahBiayaPesanan) AS java.lang.Long)) "
                        +
                        "FROM Pesanan p " +
                        "WHERE EXTRACT(YEAR FROM p.tanggalPemesanan) = :tahun " +
                        "AND EXTRACT(MONTH FROM p.tanggalPemesanan) = :bulan " +
                        "AND p.statusPesanan = 'Complete' " +
                        "GROUP BY p.user")
        List<RekapAgent> getAgentSummaryByMonthAndYear(@Param("bulan") int bulan, @Param("tahun") int tahun);

        @Query("SELECT p FROM Pesanan p " + "WHERE p.user.id = :userId " +
                        "AND EXTRACT(YEAR FROM p.tanggalPemesanan) = :tahun " +
                        "AND EXTRACT(MONTH FROM p.tanggalPemesanan) = :bulan")
        List<Pesanan> findOrdersByUserIdAndMonthAndYear(@Param("userId") Long userId,
                        @Param("bulan") int bulan,
                        @Param("tahun") int tahun);

        List<Pesanan> findOrdersByUserIdAndStatusPesanan(Long userId, String statusPesanan);

        List<Pesanan> findByUserId(Long userId);

        @Query("SELECT p FROM Pesanan p WHERE " +
                        "(:userId IS NULL OR p.user.id = :userId) AND " +
                        "(:bulan IS NULL OR :tahun IS NULL OR " +
                        "(MONTH(p.tanggalPemesanan) = :bulan AND YEAR(p.tanggalPemesanan) = :tahun)) AND " +
                        "(:statusPesanan IS NULL OR p.statusPesanan = :statusPesanan)")
        List<Pesanan> findByUserIdAndMonthAndYearAndStatus(@Param("userId") Long userId,
                        @Param("bulan") Integer bulan,
                        @Param("tahun") Integer tahun,
                        @Param("statusPesanan") String statusPesanan);

        void deleteByKlien(Klien klien); // kalo delete klien, pesanan yg associate jg akan kehapus

        void deleteByUser(User user); // kalo delete karyawan, pesanan yang associate jg akan kehapus
}
