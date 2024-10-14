package com.example.auction.service;

import com.example.auction.entity.Item;
import com.example.auction.repository.BidRepository;
import com.example.auction.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional; // Đảm bảo sử dụng import này

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    public Page<Item> getActiveItems(int start , int pagesSize){
        Pageable pageable = PageRequest.of(start,pagesSize);
        return itemRepository.findItemCurrentlyActive(LocalDateTime.now() , pageable);
    }

    public  Item getItemByIdWithLock(Long id){
        return itemRepository.findByIdWithLock(id).orElseThrow(() -> new EntityNotFoundException("Bid not found with id: " + id));
    }

    public Item getItemById(Long id){
        return itemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Bid not found with Id" +id)
        );
    }

    public Double getHighestBidByItemId(Long id){
        Double amount = bidRepository.findHighestBidByItemId(id);
        if(amount == null) amount = 0.0;
        return amount;
    }

}
