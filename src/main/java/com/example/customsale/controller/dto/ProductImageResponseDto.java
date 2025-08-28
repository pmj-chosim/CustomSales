package com.example.customsale.controller.dto;

import com.example.customsale.repository.entity.ProductImage;
import com.example.customsale.repository.entity.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductImageResponseDto {
    private Integer id;
    private String name;
    private String url;
    private String createdAt;
    private String updatedAt;
    private ReviewStatus latestReviewStatus;

    public static ProductImageResponseDto from(ProductImage productImage) {
        return new ProductImageResponseDto(
                productImage.getId(),
                productImage.getName(),
                productImage.getUrl(),
                productImage.getCreatedAt().toString(),
                productImage.getUpdatedAt().toString(),
                productImage.getLatestReviewStatus()
        );
    }
}
