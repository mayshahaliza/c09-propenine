package com.propenine.siku.model;

import com.propenine.siku.modelstok.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rekap_penjualan")
public class RekapPenjualan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", referencedColumnName = "idProduct")
    private Product product;

    @Column(name="total_quantity", nullable = false)
    private Integer totalQuantity;

    // Constructor
    public RekapPenjualan(Product product, Long totalQuantity) {
        this.product = product;
        this.totalQuantity = totalQuantity.intValue(); // Convert Long to Integer
    }
}
