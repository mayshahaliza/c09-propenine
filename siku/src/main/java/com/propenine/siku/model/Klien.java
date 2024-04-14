package com.propenine.siku.model;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "klien")
@SQLDelete(sql = "UPDATE klien SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Klien {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // atau GenerationType.UUID jika tersedia
    private UUID id;

    @NotNull
    @Column(name = "nama_klien")
    private String namaKlien;

    @NotNull
    @Column(name = "rumah_sakit")
    private String rumahSakit;

    @NotNull
    @Column(name = "email_klien")
    private String emailKlien;

    @NotNull
    @Column(name = "no_hp_klien")
    private String noHpKlien;

    @NotNull
    @Column(name = "alamat_klien")
    private String alamatKlien;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;
}
