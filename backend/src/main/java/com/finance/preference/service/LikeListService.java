package com.finance.preference.service;

import com.finance.preference.dto.LikeListRequest;
import com.finance.preference.dto.LikeListResponse;
import com.finance.preference.entity.LikeList;
import com.finance.preference.entity.Product;
import com.finance.preference.exception.BusinessException;
import com.finance.preference.repository.LikeListRepository;
import com.finance.preference.repository.ProductRepository;
import com.finance.preference.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeListService {

        private final LikeListRepository likeListRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;

        // 新增喜好商品
        @Transactional
        public void addLikeProduct(LikeListRequest req) {
                // 檢查 User 是否存在
                userRepository.findById(req.getUserId())
                                .orElseThrow(() -> new BusinessException("User not found"));

                // 檢查 Product 是否存在
                Product product = productRepository.findById(req.getProductNo())
                                .orElseThrow(() -> new BusinessException("Product not found"));

                // 業務計算 (金額四捨五入至小數第 2 位)
                BigDecimal qty = new BigDecimal(req.getPurchaseQuantity());
                BigDecimal totalFee = product.getPrice().multiply(qty).multiply(product.getFeeRate())
                                .setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalAmount = product.getPrice().multiply(qty).add(totalFee)
                                .setScale(2, RoundingMode.HALF_UP);

                // 寫入資料庫
                LikeList likeList = new LikeList();
                likeList.setUserId(req.getUserId());
                likeList.setProductNo(req.getProductNo());
                likeList.setPurchaseQuantity(req.getPurchaseQuantity());
                likeList.setAccount(req.getAccount());
                likeList.setTotalFee(totalFee);
                likeList.setTotalAmount(totalAmount);

                likeListRepository.save(likeList);
        }

        // 查詢
        @Transactional(readOnly = true)
        public List<LikeListResponse> getLikeList(String userId) {
                return likeListRepository.findLikeListResponsesByUserId(userId);
        }

        // 刪除
        @Transactional
        public void deleteLikeProduct(Integer sn, String userId) {
                LikeList likeList = likeListRepository.findBySnAndUserId(sn, userId)
                                .orElseThrow(() -> new BusinessException("Record not found or access denied"));
                likeListRepository.delete(likeList);
        }

        // 修改
        @Transactional
        public void updateLikeProduct(Integer sn, LikeListRequest req) {
                // 確認紀錄存在且屬於該使用者
                LikeList likeList = likeListRepository.findBySnAndUserId(sn, req.getUserId())
                                .orElseThrow(() -> new BusinessException("Record not found or access denied"));

                // 取得新產品資料
                Product product = productRepository.findById(req.getProductNo())
                                .orElseThrow(() -> new BusinessException("Product not found"));

                // 計算總金額與手續費
                BigDecimal qty = new BigDecimal(req.getPurchaseQuantity());
                BigDecimal totalFee = product.getPrice().multiply(qty).multiply(product.getFeeRate())
                                .setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalAmount = product.getPrice().multiply(qty).add(totalFee)
                                .setScale(2, RoundingMode.HALF_UP);

                likeList.setProductNo(req.getProductNo());
                likeList.setPurchaseQuantity(req.getPurchaseQuantity());
                likeList.setAccount(req.getAccount());
                likeList.setTotalFee(totalFee);
                likeList.setTotalAmount(totalAmount);

                likeListRepository.save(likeList);
        }
}
