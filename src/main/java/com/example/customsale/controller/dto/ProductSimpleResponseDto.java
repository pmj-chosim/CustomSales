package com.example.customsale.controller.dto;

import com.example.customsale.repository.entity.ImageReviewHistory;
import com.example.customsale.repository.entity.Product;
import com.example.customsale.repository.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder; // Lombok Builder 추가
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder // DTO 생성을 위한 Builder 패턴 추가
public class ProductSimpleResponseDto {

    private Integer id;
    private Integer userId;
    private String userName;
    private Integer baseProductId;
    private String baseProductName;
    private List<Integer> productImageIds;
    private String name;
    private String createdAt;
    private List<ImageReviewHistoryDto> reviewHistories;

    // 1. 기존 메서드는 그대로 유지 (내부용)
    public static ProductSimpleResponseDto from(Product product, List<ImageReviewHistory> histories){
        return new ProductSimpleResponseDto(
                product.getId(),
                product.getUser().getId(),
                product.getUser().getName(),
                product.getBaseProduct().getId(),
                product.getBaseProduct().getName(),
                product.getProductImages().stream().map(ProductImage::getId).toList(),
                product.getName(),
                product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                histories.stream().map(ImageReviewHistoryDto::from).collect(Collectors.toList())
        );
    }

    // 2. 새로운 메서드 추가 (외부용)
    public static ProductSimpleResponseDto from(Product product) {
        return ProductSimpleResponseDto.builder()
                .id(product.getId())
                .userId(product.getUser().getId())
                .userName(product.getUser().getName())
                .baseProductId(product.getBaseProduct().getId())
                .baseProductName(product.getBaseProduct().getName())
                .productImageIds(product.getProductImages().stream().map(ProductImage::getId).toList())
                .name(product.getName())
                .createdAt(product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .reviewHistories(null) // 히스토리는 null로 설정하거나 제거
                .build();
    }
}