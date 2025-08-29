package com.example.customsale.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "상품 등록 Dto")
@AllArgsConstructor
@Getter
public class ProductCreateRequestDto {
    @Schema(name = "베이스 상품 ID", example = "1")
    private Integer baseProductId;

    @Schema(name="사진 URL 리스트", example="[\"http://~~\",\"https://123.com\"]")
    @Size(min = 1, max = 5, message = "사진은 최소 1장, 최대 5장이어야 합니다.")
    private List<String> imageUrls; // 사진 url 받음.

    @Schema(name="상품 이름", example="귀여운 고양이 디자인 머그컵")
    @Size(max = 20, message = "상품 이름은 최대 20자 이하여야 합니다.")
    private String name;
}
