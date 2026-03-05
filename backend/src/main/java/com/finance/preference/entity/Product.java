package com.finance.preference.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "Products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "No")
    private Integer no; // pk 產品流水號

    @Column(name = "ProductName", nullable = false, length = 100)
    private String productName; // 產品名稱

    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price; // 產品價格

    @Column(name = "FeeRate", nullable = false, precision = 8, scale = 6)
    private BigDecimal feeRate; // 手續費率
}
