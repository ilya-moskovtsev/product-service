package com.product.service;

import java.math.BigDecimal;

public class ProductTestConstants {
    public static final String WRONG_PRODUCT_CODE = "wrong_product_code";
    public static final String SOME_PRODUCT_CODE = "some_product_code";
    public static final String PRODUCT_A_PRODUCT_CODE = "123-ABC";
    public static final String PRODUCT_B_PRODUCT_CODE = "456-DEF";
    public static final String PRODUCT_C_PRODUCT_CODE = "789-GHI";
    public static final String PRODUCT_A_MARKETPLACE = "NL";
    public static final String PRODUCT_B_MARKETPLACE = "GB";
    public static final String PRODUCT_C_MARKETPLACE = "FR";
    public static final String PRODUCT_A_STATUS = "active";
    public static final String PRODUCT_B_STATUS = "inactive";
    public static final String PRODUCT_C_STATUS = "pending";
    public static final BigDecimal PRODUCT_A_PRICE = BigDecimal.valueOf(21.56d);
    public static final BigDecimal PRODUCT_B_PRICE = BigDecimal.valueOf(22.56d);
    public static final BigDecimal PRODUCT_C_PRICE = BigDecimal.valueOf(23.56d);
    public static final long VALID_PRODUCT_ID = 11L;
    public static final long INVALID_PRODUCT_ID = -99L;
    public static final Product PRODUCT_A = new Product(PRODUCT_A_PRODUCT_CODE, PRODUCT_A_PRICE, PRODUCT_A_MARKETPLACE, PRODUCT_A_STATUS);
    public static final Product PRODUCT_B = new Product(PRODUCT_B_PRODUCT_CODE, PRODUCT_B_PRICE, PRODUCT_B_MARKETPLACE, PRODUCT_B_STATUS);
    public static final Product PRODUCT_C = new Product(PRODUCT_C_PRODUCT_CODE, PRODUCT_C_PRICE, PRODUCT_C_MARKETPLACE, PRODUCT_C_STATUS);
    public static final Product PRODUCT_WITHOUT_PRODUCT_CODE = new Product(null, PRODUCT_A_PRICE, PRODUCT_A_MARKETPLACE, PRODUCT_A_STATUS);
    public static final Product PRODUCT_WITH_BLANK_PRODUCT_CODE = new Product("", PRODUCT_A_PRICE, PRODUCT_A_MARKETPLACE, PRODUCT_A_STATUS);
    public static final Product PRODUCT_WITHOUT_PRICE = new Product(PRODUCT_A_PRODUCT_CODE, null, PRODUCT_A_MARKETPLACE, PRODUCT_A_STATUS);
    public static final Product PRODUCT_WITHOUT_MARKETPLACE = new Product(PRODUCT_A_PRODUCT_CODE, PRODUCT_A_PRICE, null, PRODUCT_A_STATUS);
    public static final Product PRODUCT_WITH_WRONG_MARKETPLACE = new Product(PRODUCT_A_PRODUCT_CODE, PRODUCT_A_PRICE, "wrong marketplace", PRODUCT_A_STATUS);
    public static final Product PRODUCT_WITHOUT_STATUS = new Product(PRODUCT_A_PRODUCT_CODE, PRODUCT_A_PRICE, PRODUCT_A_MARKETPLACE, null);
    public static final Product PRODUCT_WITH_WRONG_STATUS = new Product(PRODUCT_A_PRODUCT_CODE, PRODUCT_A_PRICE, PRODUCT_A_MARKETPLACE, "wrong status");
}
