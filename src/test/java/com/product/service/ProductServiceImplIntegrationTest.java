package com.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ProductServiceImplIntegrationTest {

    @TestConfiguration
    static class ProductServiceImplTestContextConfiguration {
        @Bean
        public ProductService productService() {
            return new ProductServiceImpl();
        }
    }

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        Product productA = ProductTestConstants.PRODUCT_A;
        productA.setId(ProductTestConstants.VALID_PRODUCT_ID);

        Product productB = ProductTestConstants.PRODUCT_B;
        Product productC = ProductTestConstants.PRODUCT_C;

        List<Product> allProducts = Arrays.asList(productA, productB, productC);

        Mockito.when(productRepository.findByProductCode(productA.getProductCode())).thenReturn(productA);
        Mockito.when(productRepository.findByProductCode(productC.getProductCode())).thenReturn(productC);
        Mockito.when(productRepository.findByProductCode(ProductTestConstants.WRONG_PRODUCT_CODE)).thenReturn(null);
        Mockito.when(productRepository.findById(productA.getId())).thenReturn(Optional.of(productA));
        Mockito.when(productRepository.findAll()).thenReturn(allProducts);
        Mockito.when(productRepository.findById(ProductTestConstants.INVALID_PRODUCT_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void whenValidProductCode_thenProductShouldBeFound() {
        String productCode = ProductTestConstants.PRODUCT_A_PRODUCT_CODE;
        Product found = productService.getProductByProductCode(productCode);

        assertThat(found.getProductCode()).isEqualTo(productCode);
    }

    @Test
    public void whenInValidProductCode_thenProductShouldNotBeFound() {
        Product fromDb = productService.getProductByProductCode(ProductTestConstants.WRONG_PRODUCT_CODE);
        assertThat(fromDb).isNull();

        verifyFindByProductCodeIsCalledOnce(ProductTestConstants.WRONG_PRODUCT_CODE);
    }

    @Test
    public void whenValidProductCode_thenProductShouldExist() {
        boolean doesProductExist = productService.exists(ProductTestConstants.PRODUCT_A_PRODUCT_CODE);
        assertThat(doesProductExist).isEqualTo(true);

        verifyFindByProductCodeIsCalledOnce(ProductTestConstants.PRODUCT_A_PRODUCT_CODE);
    }

    @Test
    public void whenNonExistingProductCode_thenProductShouldNotExist() {
        boolean doesProductExist = productService.exists(ProductTestConstants.SOME_PRODUCT_CODE);
        assertThat(doesProductExist).isEqualTo(false);

        verifyFindByProductCodeIsCalledOnce(ProductTestConstants.SOME_PRODUCT_CODE);
    }

    @Test
    public void whenValidId_thenProductShouldBeFound() {
        Product fromDb = productService.getProductById(ProductTestConstants.VALID_PRODUCT_ID);
        assertThat(fromDb.getProductCode()).isEqualTo(ProductTestConstants.PRODUCT_A_PRODUCT_CODE);

        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void whenInValidId_thenProductShouldNotBeFound() {
        Product fromDb = productService.getProductById(ProductTestConstants.INVALID_PRODUCT_ID);
        verifyFindByIdIsCalledOnce();
        assertThat(fromDb).isNull();
    }

    @Test
    public void given3Products_whenGetAll_thenReturn3Records() {
        Product productA = ProductTestConstants.PRODUCT_A;
        Product productB = ProductTestConstants.PRODUCT_B;
        Product productC = ProductTestConstants.PRODUCT_C;

        List<Product> allProducts = productService.getAllProducts();
        verifyFindAllProductsIsCalledOnce();
        assertThat(allProducts)
                .hasSize(3)
                .extracting(Product::getProductCode)
                .contains(productA.getProductCode(), productB.getProductCode(), productC.getProductCode());
    }

    private void verifyFindByProductCodeIsCalledOnce(String productCode) {
        Mockito.verify(productRepository, VerificationModeFactory.times(1)).findByProductCode(productCode);
        Mockito.reset(productRepository);
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(productRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
        Mockito.reset(productRepository);
    }

    private void verifyFindAllProductsIsCalledOnce() {
        Mockito.verify(productRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(productRepository);
    }
}