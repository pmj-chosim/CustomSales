package com.example.customsale.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "base_product_id")
    private BaseProduct baseProduct;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<ProductImage> productImages;

    private String name;
    private LocalDateTime createdAt;

    public static Product create(User user, BaseProduct baseProduct, String name) {
        return new Product(null, user, baseProduct, Collections.emptyList(), name, LocalDateTime.now());
    }


}
