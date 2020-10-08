package com.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product getProductByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    @Override
    public List<Product> getAllProductsByStatus(String status) {
        return productRepository.findAllByStatus(status);
    }

    @Override
    public boolean exists(String productCode) {
        return productRepository.findByProductCode(productCode) != null;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProductPrice(Product product) {
        Product found = productRepository.findByProductCode(product.getProductCode());
        found.setPrice(product.getPrice());
        return productRepository.saveAndFlush(found);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
