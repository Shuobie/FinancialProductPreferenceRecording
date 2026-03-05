package com.finance.preference.repository;

import com.finance.preference.entity.LikeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeListRepository extends JpaRepository<LikeList, Integer> {
    List<LikeList> findAllByUserIdOrderBySnAsc(String userId);

    // 把 LikeList (代號 l)、Product (代號 p) 和 User (代號 u) 三個實體關聯。
    // 產品編號 (l.productNo = p.no) 和使用者 ID (l.userId = u.userId) 進行比對
    @Query("SELECT new com.finance.preference.dto.LikeListResponse(" +
            "l.sn, p.productName, p.price, p.feeRate, l.purchaseQuantity, " +
            "l.account, l.totalFee, l.totalAmount, u.email) " +
            "FROM LikeList l " +
            "JOIN Product p ON l.productNo = p.no " +
            "JOIN User u ON l.userId = u.userId " +
            "WHERE l.userId = :userId ORDER BY l.sn ASC")
    List<com.finance.preference.dto.LikeListResponse> findLikeListResponsesByUserId(@Param("userId") String userId);

    // 透過流水號 (BySn) 和 使用者 ID (UserId) 來尋找唯一的一筆資料
    Optional<LikeList> findBySnAndUserId(Integer sn, String userId);
}
