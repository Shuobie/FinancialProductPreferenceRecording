package com.finance.preference.controller;

import com.finance.preference.dto.ApiResponse;
import com.finance.preference.entity.Product;
import com.finance.preference.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // GET /api/products — 取得所有產品 (供前端下拉選單)
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(productService.getAllProducts()));
    }
}
