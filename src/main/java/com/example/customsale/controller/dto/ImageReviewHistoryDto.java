package com.example.customsale.controller.dto;

import com.example.customsale.repository.entity.ImageReviewHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class ImageReviewHistoryDto {
    private String status;
    private String createdAt;

    public static ImageReviewHistoryDto from(ImageReviewHistory history) {
        return new ImageReviewHistoryDto(
                history.getReviewStatus().toString(),
                history.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}