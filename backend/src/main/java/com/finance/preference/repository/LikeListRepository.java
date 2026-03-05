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

    @Query("SELECT new com.finance.preference.dto.LikeListResponse(" +
            "l.sn, p.productName, p.price, p.feeRate, l.purchaseQuantity, " +
            "l.account, l.totalFee, l.totalAmount, u.email) " +
            "FROM LikeList l " +
            "JOIN Product p ON l.productNo = p.no " +
            "JOIN User u ON l.userId = u.userId " +
            "WHERE l.userId = :userId ORDER BY l.sn ASC")
    List<com.finance.preference.dto.LikeListResponse> findLikeListResponsesByUserId(@Param("userId") String userId);

    Optional<LikeList> findBySnAndUserId(Integer sn, String userId);
}
