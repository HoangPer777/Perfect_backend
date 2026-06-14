package com.perfectmarket.modules.service;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.product.Product;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "service_packages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "designer_id")
    private User designer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String title;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private PackageType packageType; // BASIC, PRO, VIP, CUSTOM

    private BigDecimal price;
    private int deliveryDays;
    private int revisionsLimit;
    
    private String status;

    public enum PackageType {
        BASIC, PRO, VIP, CUSTOM
    }

    // TODO: Add package features list (e.g., source file included, high res)
}
