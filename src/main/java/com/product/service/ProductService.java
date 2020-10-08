package com.product.service;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id);

    Product getProductByProductCode(String productCode);

    List<Product> getAllProductsByStatus(String status);

    List<Product> getAllProducts();

    boolean exists(String productCode);

    Product save(Product product);

    Product updateProductPrice(Product product);

}
