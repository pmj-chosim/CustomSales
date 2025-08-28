package com.example.customsale.controller.dto;

import com.example.customsale.repository.entity.BaseProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseProductResponseDto {
    private Integer id;
    private String name;
    private String price;
    private String createdAt;

    public static BaseProductResponseDto from(BaseProduct baseProduct) {
        return new BaseProductResponseDto(
                baseProduct.getId(),
                baseProduct.getName(),
                baseProduct.getPrice().toString(),
                baseProduct.getCreatedAt().toString()
        );
    }
}
