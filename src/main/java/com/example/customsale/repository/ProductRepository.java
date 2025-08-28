package com.example.customsale.repository;

import com.example.customsale.repository.entity.Product;
import com.example.customsale.repository.entity.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByUserId(Integer userId);

    //  모든 이미지가 특정 상태인 상품을 조회하는 메서드
    @Query("SELECT p FROM Product p JOIN p.productImages pi WHERE pi.latestReviewStatus = :status GROUP BY p.id HAVING COUNT(pi) = (SELECT COUNT(pi2) FROM ProductImage pi2 WHERE pi2.product.id = p.id)")
    Page<Product> findApprovedProducts(@Param("status") ReviewStatus status, Pageable pageable);

}