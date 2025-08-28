package com.example.customsale.controller;


import com.example.customsale.controller.dto.*;
import com.example.customsale.service.ProductService;
import com.example.customsale.repository.entity.User;
import com.example.customsale.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

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

    //SecurityContextHolder
    //principal 구현에 따라 내가 가져올 게 다름..

    // 4. 로그인된 유저가 등록한 상품 조회 API
    @GetMapping("/my")
    public ResponseEntity<List<ProductSimpleResponseDto>> getMyProducts(Principal principal) {
        // Principal 객체를 통해 현재 인증된 사용자의 이름을 가져옵니다.
        String username = principal.getName();
        // UserService를 사용해 사용자 이름을 기반으로 ID를 조회합니다.
        /* 서비스에 유저가 있는지 검증 uR.~~로
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/
        UserResponseDto user = userService.getUserIdByName(username);
        List<ProductSimpleResponseDto> myProducts = productService.findProductsByUserId(user.getId());
        return ResponseEntity.ok(myProducts);
    }


    // 3. 상품 생성 API - Principal 객체를 사용하여 userId를 안전하게 가져옵니다.
    @PostMapping
    public ResponseEntity<ProductDetailResponseDto> createProduct(
            Principal principal, // 현재 인증된 사용자의 정보를 담고 있습니다.
            @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        // principal.getName()으로 UserDetailsService가 반환한 사용자의 name(username)을 가져옵니다.
        String username = principal.getName();
        // UserService에서 사용자 ID를 조회합니다.
        UserResponseDto user = userService.getUserByName(username);
        // 이제 안전하게 userId를 서비스 레이어로 전달합니다.
        /* 서비스에서 처리
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        */
        ProductDetailResponseDto createdProduct = productService.createProduct(user.getId(), productCreateRequestDto);
        return ResponseEntity.ok(createdProduct);
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
