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
@Table(name = "stok")
public class Stok {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idKatalog;

    @NotNull
    @Column(name = "nama_katalog")
    private String namaKatalog;


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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Kategori kategori;
}
