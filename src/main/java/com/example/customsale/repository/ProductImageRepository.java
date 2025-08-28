package com.example.customsale.repository;

import com.example.customsale.repository.entity.Product;
import com.example.customsale.repository.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {


}
