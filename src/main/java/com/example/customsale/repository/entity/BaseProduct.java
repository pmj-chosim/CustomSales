package com.example.customsale.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BaseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private BigDecimal price;
    private LocalDateTime createdAt;

    // BaseProduct가 여러 Product를 가질 수 있으므로 @OneToMany를 사용하고 mappedBy를 지정
    @OneToMany(mappedBy = "baseProduct", fetch = FetchType.EAGER)
    private List<Product> products;

    // BaseProduct가 여러 BaseOptions를 가질 수 있으므로 @OneToMany를 사용
    @OneToMany(mappedBy = "baseProduct", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BaseOptions> baseOptions;
}





