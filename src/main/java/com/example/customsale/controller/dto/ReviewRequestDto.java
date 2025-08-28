package com.example.customsale.controller.dto;

import com.example.customsale.repository.entity.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewRequestDto {
    private ReviewStatus status;
}
