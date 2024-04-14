package com.propenine.siku.modelstok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

import com.propenine.siku.model.katalog.Kategori;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idProduct;

    @NotNull
    @Column(name = "nama_product")
    private String namaProduct;

    @NotNull
    @Column(name = "stok")
    private Integer stok;

    @NotNull
    @Column(name = "harga")
    private Integer harga;

    @NotNull
    @Column(name = "status")
    private Boolean status;

    @NotNull
    @Column(name = "deskripsi")
    private String deskripsi;

    @Lob
    @Column(name = "image")
    @Basic(fetch = FetchType.LAZY)
    private String image;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Kategori kategori;
}
