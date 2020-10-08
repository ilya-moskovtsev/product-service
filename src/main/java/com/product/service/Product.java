package com.product.service;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @NotBlank(message = "productCode is mandatory")
    private String productCode;
    @NotNull(message = "price is mandatory")
    private BigDecimal price;
    @NotBlank(message = "marketplace is mandatory")
    @Pattern(regexp = "NL|GB|FR", message = "marketplace expected values are NL, GB or FR")
    private String marketplace;
    @NotBlank(message = "status is mandatory")
    @Pattern(regexp = "active|inactive|pending", message = "status expected values are active, inactive or pending")
    private String status;

    public Product(String productCode, BigDecimal price, String marketplace, String status) {
        this.productCode = productCode;
        this.price = price;
        this.marketplace = marketplace;
        this.status = status;
    }
}
