package com.example.auction.repository;

import com.example.auction.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("select max(b.amount) from Bid b where b.item.id = :itemId")
    Double findHighestBidByItemId(@Param("itemId") Long itemId);

    @EntityGraph(attributePaths = {"user", "item"})
    @Query("SELECT b FROM Bid b WHERE b.user.id = :userId")
    Page<Bid> findBidByUserId(@Param("userId") Long userId, Pageable pageable );

    @Override
    Optional<Bid> findById(Long aLong);

    @Modifying
    @Query("DELETE FROM Bid b WHERE b.id = :bidId AND b.user.id = :userId")
    int deleteBidById(@Param("bidId") Long bidId, @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user"})
    Page<Bid> findByItemId(Long itemId, Pageable pageable);

}
