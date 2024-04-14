package com.propenine.siku.model;

import com.propenine.siku.modelstok.Product;
import com.propenine.siku.model.Klien;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rekap_klien")
public class RekapKlien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_klien", referencedColumnName = "id")
    private Klien klien;

    @Column(name="total_quantity", nullable = false)
    private Integer totalQuantity;

    // Constructor
    public RekapKlien(Klien klien, Long totalQuantity) {
        this.klien = klien;
        this.totalQuantity = totalQuantity.intValue(); // Convert Long to Integer
    }
}
