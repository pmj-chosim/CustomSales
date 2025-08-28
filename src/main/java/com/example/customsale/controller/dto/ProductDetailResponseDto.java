package com.example.customsale.controller.dto;


import com.example.customsale.repository.entity.BaseProduct;
import com.example.customsale.repository.entity.Product;
import com.example.customsale.repository.entity.ProductImage;
import com.example.customsale.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ProductDetailResponseDto {
    private Integer id;
    private String name;
    private Integer userId;
    private String userName;
    private BaseProductResponseDto baseProduct;
    private List<ProductImageResponseDto> productImages;


    public static ProductDetailResponseDto from(Product product,User user, BaseProduct baseProduct, List<ProductImage> productImages){



        // ProductImage 리스트를 ProductImageResponseDto 리스트로 변환
        List<ProductImageResponseDto> productImageDtos = productImages.stream()
                .map(ProductImageResponseDto::from)
                .collect(Collectors.toList());

        return new ProductDetailResponseDto(
                product.getId(),
                product.getName(),
                user.getId(),
                user.getName(),
                BaseProductResponseDto.from(baseProduct),
                productImageDtos
        );
    }


}
