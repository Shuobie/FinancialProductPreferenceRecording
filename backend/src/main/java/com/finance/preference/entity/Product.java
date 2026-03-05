package com.finance.preference.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "No")
    private Integer no;

    @Column(name = "ProductName", nullable = false, length = 100)
    private String productName;

    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "FeeRate", nullable = false, precision = 8, scale = 6)
    private BigDecimal feeRate;
}
