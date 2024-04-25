package com.propenine.siku.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rekap_agent")
public class RekapAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @Column(name="total_quantity", nullable = false)
    private Integer totalQuantity;

    // Constructor
    public RekapAgent(User user, Long totalQuantity) {
        this.user = user;
        this.totalQuantity = totalQuantity.intValue(); // Convert Long to Integer
    }
}
