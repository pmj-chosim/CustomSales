package com.example.customsale.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String url;

    @Enumerated(EnumType.STRING)
    private ReviewStatus latestReviewStatus;

    @Builder.Default // 이 어노테이션을 추가하여 빌더가 초기화식 사용
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "productImage", orphanRemoval = true)
    private List<ImageReviewHistory> reviewHistories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 새로운 히스토리 추가 메서드
    public void addReviewHistory(ImageReviewHistory history) {
        this.reviewHistories.add(history);
        history.setProductImage(this);
    }
}