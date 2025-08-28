package com.example.customsale.repository.entity;

import jakarta.persistence.*;
import lombok.*; // Lombok 패키지 전체 임포트
import java.time.LocalDateTime;

@Entity
@Getter
@Setter // 양방향 관계 설정을 위해 필요
@Builder // 빌더 패턴 사용
@NoArgsConstructor // @Builder 사용을 위해 기본 생성자 필요
@AllArgsConstructor
public class ImageReviewHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_image_id")
    private ProductImage productImage;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    private LocalDateTime createdAt;

}