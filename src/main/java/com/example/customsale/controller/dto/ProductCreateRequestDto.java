package com.example.customsale.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.Size;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProductCreateRequestDto {
    private Integer baseProductId;

    @Size(min = 1, max = 5, message = "사진은 최소 1장, 최대 5장이어야 합니다.")
    private List<String> imageUrls; // 사진 url 받음.

    @Size(max = 20, message = "상품 이름은 최대 20자 이하여야 합니다.")
    private String name;
}
