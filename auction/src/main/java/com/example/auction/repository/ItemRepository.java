package com.example.auction.repository;

import com.example.auction.entity.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("Select i from Item i where i.startDateTime <= :now and i.endDateTime >= :now")
    Page<Item> findItemCurrentlyActive(@Param("now")LocalDateTime now , Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Optional<Item> findByIdWithLock(@Param("id") Long id);

}
