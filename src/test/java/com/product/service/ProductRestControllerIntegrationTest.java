package com.product.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ServiceApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
class ProductRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void whenValidInput_thenCreateProduct() throws Exception {
        mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(ProductTestConstants.PRODUCT_A)));

        List<Product> found = repository.findAll();
        assertThat(found).extracting(Product::getProductCode).containsOnly(ProductTestConstants.PRODUCT_A_PRODUCT_CODE);
    }

    @Test
    public void givenProducts_whenGetProducts_thenStatus200() throws Exception {
        repository.save(ProductTestConstants.PRODUCT_B);
        repository.save(ProductTestConstants.PRODUCT_C);
        repository.flush();

        mvc.perform(get("/api/products").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].productCode", is(ProductTestConstants.PRODUCT_B_PRODUCT_CODE)))
                .andExpect(jsonPath("$[1].productCode", is(ProductTestConstants.PRODUCT_C_PRODUCT_CODE)));
    }

    @Test
    public void givenProduct_whenGetProductByProductCode_thenStatus200() throws Exception {
        repository.saveAndFlush(ProductTestConstants.PRODUCT_C);

        mvc.perform(get("/api/products/productcode/" + ProductTestConstants.PRODUCT_C_PRODUCT_CODE)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productCode", is(ProductTestConstants.PRODUCT_C_PRODUCT_CODE)));
    }

    @Test
    public void givenProduct_whenGetProductsByStatus_thenStatus200() throws Exception {
        repository.save(ProductTestConstants.PRODUCT_A);
        repository.save(ProductTestConstants.PRODUCT_B);
        repository.saveAndFlush(ProductTestConstants.PRODUCT_C);

        mvc.perform(get("/api/products/status/" + ProductTestConstants.PRODUCT_B_STATUS)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productCode", is(ProductTestConstants.PRODUCT_B_PRODUCT_CODE)));
    }

    @Test
    public void givenProduct_whenUpdateProductPrice_thenPriceUpdated() throws Exception {
        Product product = ProductTestConstants.PRODUCT_B;
        repository.saveAndFlush(product);

        BigDecimal expectedPrice = BigDecimal.valueOf(12.34d);
        product.setPrice(expectedPrice);

        mvc.perform(put("/api/products/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(product)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productCode", is(ProductTestConstants.PRODUCT_B_PRODUCT_CODE)))
                .andExpect(jsonPath("$.price", not(ProductTestConstants.PRODUCT_B_PRICE.doubleValue())))
                .andExpect(jsonPath("$.price", is(expectedPrice.doubleValue())));

        mvc.perform(get("/api/products/productcode/" + ProductTestConstants.PRODUCT_B_PRODUCT_CODE)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productCode", is(ProductTestConstants.PRODUCT_B_PRODUCT_CODE)))
                .andExpect(jsonPath("$.price", is(expectedPrice.doubleValue())));
    }

    @Test
    public void whenPostRequestToProductsAndWithoutProductCode_thenCorrectResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(JsonUtil.toJson(ProductTestConstants.PRODUCT_WITHOUT_PRODUCT_CODE))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productCode", is("productCode is mandatory")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToProductsAndProductCodeIsBlank_thenCorrectResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(JsonUtil.toJson(ProductTestConstants.PRODUCT_WITH_BLANK_PRODUCT_CODE))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productCode", is("productCode is mandatory")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToProductsAndWithoutPrice_thenCorrectResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(JsonUtil.toJson(ProductTestConstants.PRODUCT_WITHOUT_PRICE))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is("price is mandatory")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToProductsAndWithoutMarketplace_thenCorrectResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(JsonUtil.toJson(ProductTestConstants.PRODUCT_WITHOUT_MARKETPLACE))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.marketplace", is("marketplace is mandatory")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToProductsAndMarketplaceIsWrong_thenCorrectResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(JsonUtil.toJson(ProductTestConstants.PRODUCT_WITH_WRONG_MARKETPLACE))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.marketplace", is("marketplace expected values are NL, GB or FR")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToProductsAndWithoutStatus_thenCorrectResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(JsonUtil.toJson(ProductTestConstants.PRODUCT_WITHOUT_STATUS))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("status is mandatory")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToProductsAndStatusIsWrong_thenCorrectResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(JsonUtil.toJson(ProductTestConstants.PRODUCT_WITH_WRONG_STATUS))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("status expected values are active, inactive or pending")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}