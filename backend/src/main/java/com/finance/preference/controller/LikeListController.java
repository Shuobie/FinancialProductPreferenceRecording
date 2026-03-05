package com.finance.preference.controller;

import com.finance.preference.dto.ApiResponse;
import com.finance.preference.dto.LikeListRequest;
import com.finance.preference.dto.LikeListResponse;
import com.finance.preference.service.LikeListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class LikeListController {

    private final LikeListService likeListService;

    // 新增
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> add(@Valid @RequestBody LikeListRequest req) {
        likeListService.addLikeProduct(req);
        return ResponseEntity.ok(ApiResponse.ok("新增成功", null));
    }

    // 查詢
    @GetMapping
    public ResponseEntity<ApiResponse<List<LikeListResponse>>> get(
            @RequestParam("userId") String userId) {
        List<LikeListResponse> list = likeListService.getLikeList(userId);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    // 刪除
    @DeleteMapping("/{sn}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("sn") Integer sn,
            @RequestParam("userId") String userId) {
        likeListService.deleteLikeProduct(sn, userId);
        return ResponseEntity.ok(ApiResponse.ok("刪除成功", null));
    }

    // 更改
    @PutMapping("/{sn}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable("sn") Integer sn,
            @Valid @RequestBody LikeListRequest req) {
        likeListService.updateLikeProduct(sn, req);
        return ResponseEntity.ok(ApiResponse.ok("更新成功", null));
    }
}
