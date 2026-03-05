package com.finance.preference.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "LikeList")
@NoArgsConstructor
@AllArgsConstructor
public class LikeList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SN")
    private Integer sn; // pk 流水序號

    @Column(name = "UserID", nullable = false, length = 20)
    private String userId; // fk 使用者id

    @Column(name = "ProductNo", nullable = false)
    private Integer productNo; // fk 產品id

    @Column(name = "PurchaseQuantity", nullable = false)
    private Integer purchaseQuantity; // 購買數量

    @Column(name = "Account", nullable = false, length = 50)
    private String account; // 銀行帳號

    @Column(name = "TotalFee", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalFee; // 手續費

    @Column(name = "TotalAmount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount; // 總金額
}