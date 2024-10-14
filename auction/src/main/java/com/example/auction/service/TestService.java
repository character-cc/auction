package com.example.auction.service;

import com.example.auction.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

    @Autowired
    ItemRepository itemRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testVersionIncrease(Long id) {
        itemRepository.findByIdWithLock(id).orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
    }
}
