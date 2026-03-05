package com.finance.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeListResponse {
    private Integer sn;
    private String  productName;
    private BigDecimal price;
    private BigDecimal feeRate;
    private Integer purchaseQuantity;
    private String  account;
    private BigDecimal totalFee;
    private BigDecimal totalAmount;
    private String  email;
}
