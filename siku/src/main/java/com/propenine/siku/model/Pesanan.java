package com.propenine.siku.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

import com.propenine.siku.modelstok.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"pesanan\"")
public class Pesanan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama client harus diisi")
    @Column(name="nama_client", nullable = false)
    private String namaClient;

    @NotBlank(message = "Nama agent harus diisi")
    @Column(name="nama_agent", nullable = false)
    private String namaAgent;

    @Value("ongoing")
    @Column(name="status_pesanan", nullable = false)
    private String statusPesanan;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", referencedColumnName = "idProduct")
    private Product product;

    @Column(name = "jumlah_barang_pesanan")
    private Integer jumlahBarangPesanan;

    @NotNull(message = "Tanggal pemesanan harus diisi")
    @Column(name = "tanggal_pemesanan", nullable = false)
    private LocalDate tanggalPemesanan;

    @Column(name = "tanggal_selesai", nullable = false)
    private LocalDate tanggalSelesai;
}
