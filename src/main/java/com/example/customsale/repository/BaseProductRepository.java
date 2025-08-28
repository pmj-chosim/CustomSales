package com.example.customsale.repository;

import com.example.customsale.repository.entity.BaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseProductRepository extends JpaRepository<BaseProduct, Integer> {

    BaseProduct findById(int id);
}
