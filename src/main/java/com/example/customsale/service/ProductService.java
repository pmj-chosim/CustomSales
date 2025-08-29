package com.example.customsale.service;

import com.example.customsale.controller.dto.ProductCreateRequestDto;
import com.example.customsale.controller.dto.ProductDetailResponseDto;
import com.example.customsale.controller.dto.ProductSimpleResponseDto;
import com.example.customsale.repository.BaseProductRepository;
import com.example.customsale.repository.ImageReviewHistoryRepository;
import com.example.customsale.repository.ProductImageRepository;
import com.example.customsale.repository.ProductRepository;
import com.example.customsale.repository.UserRepository;
import com.example.customsale.repository.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository pR;
    private final UserRepository uR;
    private final BaseProductRepository bR;
    private final ProductImageRepository piR;
    private final ImageReviewHistoryRepository irhR;

    @Transactional(readOnly = true)
    public ProductDetailResponseDto findProductDetailById(Integer id) {
        Product product = pR.findById(id).orElseThrow(() -> new RuntimeException("Product id:" + id + " not found"));
        User user = product.getUser();
        BaseProduct baseProduct = product.getBaseProduct();
        List<ProductImage> productImages = product.getProductImages();

        return ProductDetailResponseDto.from(product, user, baseProduct, productImages);
    }

    // 5. 성공적으로 승인받은 모든 상품 조회 API
    @Transactional(readOnly = true)
    public Page<ProductSimpleResponseDto> findAllApprovedProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        // findApprovedProducts는 ProductRepository에 직접 쿼리 정의
        Page<Product> approvedProductsPage = pR.findApprovedProducts(ReviewStatus.APPROVED, pageable);

        return approvedProductsPage.map(ProductSimpleResponseDto::from);
    }

    // 4. 로그인된 유저가 등록한 상품 조회 API
    @Transactional(readOnly = true)
    public List<ProductSimpleResponseDto> findProductsByUserId(Integer userId) {
        List<Product> products = pR.findByUserId(userId);

        User user= uR.findById(userId).orElseThrow(()->new RuntimeException("유저"+userId+"가 존재하지 않습니다"));

        return products.stream()
                .map(product -> {
                    // 모든 이미지의 리뷰 히스토리들을 취합
                    List<ImageReviewHistory> allHistories = product.getProductImages().stream()
                            .flatMap(image -> image.getReviewHistories().stream())
                            .collect(Collectors.toList());

                    return ProductSimpleResponseDto.from(product, allHistories);
                })
                .collect(Collectors.toList());
    }

    // 3. 상품 생성 API
    @Transactional
    public ProductDetailResponseDto createProduct(Integer userId, ProductCreateRequestDto productCreateRequestDto) {
        User user = uR.findById(userId)
                .orElseThrow(() -> new RuntimeException("User id:" + userId + " not found"));
        BaseProduct baseProduct = bR.findById(productCreateRequestDto.getBaseProductId())
                .orElseThrow(() -> new RuntimeException("BaseProduct id:" + productCreateRequestDto.getBaseProductId() + " not found"));

        Product product = Product.builder()
                .user(user)
                .baseProduct(baseProduct)
                .name(productCreateRequestDto.getName())
                .createdAt(LocalDateTime.now())
                .build();
        Product savedProduct = pR.save(product);

        List<ProductImage> productImages = productCreateRequestDto.getImageUrls().stream()
                .map(url -> {
                    ProductImage image = ProductImage.builder()
                            .url(url)
                            .product(savedProduct)
                            .latestReviewStatus(ReviewStatus.REGISTERED)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    // 헬퍼 메서드를 사용해 안전하게 관계 설정
                    ImageReviewHistory history = ImageReviewHistory.builder()
                            .reviewStatus(ReviewStatus.REGISTERED)
                            .createdAt(LocalDateTime.now())
                            .build();
                    image.addReviewHistory(history);

                    return image;
                })
                .collect(Collectors.toList());

        // cascadeALL 이므로 ProductImage 저장 시 History도 함께 저장됨
        piR.saveAll(productImages);

        return ProductDetailResponseDto.from(savedProduct, user, baseProduct, productImages);
    }

    // 6. 상품 내부 리뷰 API
    @Transactional
    public ProductDetailResponseDto reviewProductStatus(Integer productId, ReviewStatus newStatus) {
        Product product = pR.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product id:" + productId + " not found"));

        validateStatusChange(product, newStatus);

        List<ImageReviewHistory> newHistories = product.getProductImages().stream()
                .map(image -> {
                    image.setLatestReviewStatus(newStatus);
                    image.setUpdatedAt(LocalDateTime.now());

                    // 새로운 히스토리 객체 생성
                    return ImageReviewHistory.builder()
                            .productImage(image)
                            .reviewStatus(newStatus)
                            .createdAt(LocalDateTime.now())
                            .build();
                })
                .collect(Collectors.toList());

        // 새로운 히스토리들을 일괄 저장 cascade되어 있어 생략
        //irhR.saveAll(newHistories);

        // 변경된 이미지 정보 저장
        piR.saveAll(product.getProductImages());

        return findProductDetailById(productId);
    }

    private void validateStatusChange(Product product, ReviewStatus newStatus) {
        boolean allImagesRegistered = product.getProductImages().stream()
                .allMatch(img -> img.getLatestReviewStatus() == ReviewStatus.REGISTERED);

        switch (newStatus) {
            case APPROVED:
                if (!allImagesRegistered) {
                    throw new RuntimeException("등록된 상태에서만 승인으로 변경 가능합니다.");
                }
                break;
            case REJECTED:
                if (!allImagesRegistered) {
                    throw new RuntimeException("등록된 상태에서만 거절로 변경 가능합니다.");
                }
                break;
            case BANNED:
                boolean anyImageApproved = product.getProductImages().stream()
                        .anyMatch(img -> img.getLatestReviewStatus() == ReviewStatus.APPROVED);
                if (!anyImageApproved) {
                    throw new RuntimeException("승인된 상태에서만 금지로 변경 가능합니다.");
                }
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 상태 변경입니다.");
        }
    }

    @Transactional
    public void deleteProduct(Integer id) {
        Product product = pR.findById(id)
                .orElseThrow(() -> new RuntimeException("Product id:" + id + " not found"));
        pR.delete(product);
    }
}