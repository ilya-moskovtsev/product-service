package com.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenFindByName_thenReturnProduct() {
        Product product = new Product("123-ABC", BigDecimal.valueOf(24.56d), "NL", "active");
        entityManager.persistAndFlush(product);

        Product found = productRepository.findByProductCode(product.getProductCode());
        assertThat(found.getProductCode()).isEqualTo(product.getProductCode());
    }

    @Test
    void whenInvalidName_thenReturnNull() {
        Product fromDb = productRepository.findByProductCode("doesNotExist");
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindById_thenReturnProduct() {
        Product product = new Product(
                "123-ABC",
                BigDecimal.valueOf(24.56d),
                "NL",
                "active"
        );
        entityManager.persistAndFlush(product);

        Product fromDb = productRepository.findById(product.getId()).orElse(null);
        assertThat(fromDb.getProductCode()).isEqualTo(product.getProductCode());
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        Product fromDb = productRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfProducts_whenFindAll_thenReturnAllProducts() {
        Product productA = new Product("123-ABC", BigDecimal.valueOf(24.56d), "NL", "active");
        Product productB = new Product("456-DEF", BigDecimal.valueOf(24.56d), "NL", "active");
        Product productC = new Product("789-GHI", BigDecimal.valueOf(24.56d), "NL", "active");

        entityManager.persist(productA);
        entityManager.persist(productB);
        entityManager.persist(productC);
        entityManager.flush();

        List<Product> allProducts = productRepository.findAll();

        assertThat(allProducts)
                .hasSize(3)
                .extracting(Product::getProductCode)
                .containsOnly(productA.getProductCode(), productB.getProductCode(), productC.getProductCode());
    }
}