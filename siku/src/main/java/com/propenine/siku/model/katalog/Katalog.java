package com.propenine.siku.model.katalog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale.Category;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "katalog")

public class Katalog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // atau GenerationType.UUID jika tersedia
    private UUID id;

    @NotNull
    @Column(name = "nama_katalog")
    private String namaKatalog;

    @NotNull
    @Column(name = "deskripsi")
    private String deskripsi;

    @NotNull
    @Column(name = "harga")
    private Integer harga;

    @Lob
    @Column(name = "image")
    @Basic(fetch = FetchType.LAZY)
    private String image;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Kategori kategori;
}
