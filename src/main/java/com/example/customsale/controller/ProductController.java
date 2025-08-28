package com.example.customsale.controller;


import com.example.customsale.controller.dto.ProductCreateRequestDto;
import com.example.customsale.controller.dto.ProductDetailResponseDto;
import com.example.customsale.controller.dto.ProductSimpleResponseDto;
import com.example.customsale.service.ProductService;
import com.example.customsale.controller.dto.ReviewRequestDto;
import com.example.customsale.repository.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductController {
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponseDto> findProductById(@PathVariable Integer id) {
        ProductDetailResponseDto product = productService.findProductDetailById(id);
        return ResponseEntity.ok(product);
    }

    // 5. 성공적으로 승인받은 모든 상품 조회 API (페이지네이션)
    @GetMapping("")
    public ResponseEntity<Page<ProductSimpleResponseDto>> findAllApproved(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<ProductSimpleResponseDto> products = productService.findAllApprovedProducts(page, size);
        return ResponseEntity.ok(products);
    }

    // 4. 로그인된 유저가 등록한 상품 조회 API
    @GetMapping("/my")
    public ResponseEntity<List<ProductSimpleResponseDto>> findProductsByUserId(
            @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ProductSimpleResponseDto> products = productService.findProductsByUserId(user.getId());
        return ResponseEntity.ok(products);
    }

    // 3. 상품 생성 API
    @PostMapping("")
    public ResponseEntity<ProductDetailResponseDto> createProduct(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProductDetailResponseDto product = productService.createProduct(user.getId(), productCreateRequestDto);
        return ResponseEntity.ok(product);
    }

    // 6. 상품 내부 리뷰 API (상태 변경)
    @PostMapping("/{id}/review")
    public ResponseEntity<ProductDetailResponseDto> reviewProduct(
            @PathVariable Integer id,
            @RequestBody ReviewRequestDto reviewRequestDto) {

        ProductDetailResponseDto updatedProduct = productService.reviewProductStatus(id, reviewRequestDto.getStatus());
        return ResponseEntity.ok(updatedProduct);
    }


    // 상품 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
