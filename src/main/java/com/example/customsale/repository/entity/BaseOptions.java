package com.example.customsale.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_allocated_base_option", columnNames = {"base_product_id", "size_id", "color_id", "material_id"})
})
@AllArgsConstructor
public class BaseOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_product_id", nullable = false)
    private BaseProduct baseProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id")
    private Material material;

}
