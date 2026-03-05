package com.finance.preference.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "LikeList")
public class LikeList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SN")
    private Integer sn;

    @Column(name = "UserID", nullable = false, length = 20)
    private String userId;

    @Column(name = "ProductNo", nullable = false)
    private Integer productNo;

    @Column(name = "PurchaseQuantity", nullable = false)
    private Integer purchaseQuantity;

    @Column(name = "Account", nullable = false, length = 50)
    private String account;

    @Column(name = "TotalFee", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalFee;

    @Column(name = "TotalAmount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;
}
