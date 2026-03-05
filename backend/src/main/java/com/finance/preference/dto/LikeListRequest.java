package com.finance.preference.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeListRequest { // 接收前端進行新增修改時傳回的資料

    @NotBlank(message = "UserID 不可為空")
    private String userId;

    @NotNull(message = "ProductNo 不可為空")
    private Integer productNo;

    @NotNull(message = "PurchaseQuantity 不可為空")
    @Min(value = 1, message = "購買數量至少為 1")
    private Integer purchaseQuantity;

    @NotBlank(message = "Account 不可為空")
    private String account;
}
