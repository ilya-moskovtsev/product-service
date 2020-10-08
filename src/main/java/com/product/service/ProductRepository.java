package com.product.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductCode(String productCode);

    List<Product> findAllByStatus(String status);

    List<Product> findAll();
}