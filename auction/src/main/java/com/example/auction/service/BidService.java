package com.example.auction.service;

import com.example.auction.entity.Bid;
import com.example.auction.entity.Item;
import com.example.auction.entity.UserSecurity;
import com.example.auction.repository.BidRepository;
import com.example.auction.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class BidService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BidRepository bidRepository;

    private static final Logger logger = LoggerFactory.getLogger(BidService.class);

    @Autowired
    TestService testService;


    public Map<String, Object> placeBid(Long itemId , Double amount){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
        Map<String, Object> response = new HashMap<>();


            Item item = itemRepository.findByIdWithLock(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

      //      testService.testVersionIncrease(itemId);
            Double highestBid = bidRepository.findHighestBidByItemId(itemId);
            highestBid = (highestBid != null) ? highestBid : 0.0;

            if (amount > highestBid) {
                Bid newBid = new Bid();
                newBid.setAmount(amount);
                newBid.setItem(item);
                newBid.setUser(userSecurity.getUser());
                newBid.setBidTime(LocalDateTime.now());

                bidRepository.save(newBid);

                response.put("newHighestBid", amount);
                response.put("success", true);
            } else {
                response.put("newHighestBid", highestBid);
                response.put("success", false);
                response.put("message", "The highest bid has changed. Current highest bid is $" + highestBid);
            }

        logger.info("Commit??");
        return response;
    }

    @PreAuthorize("#userId == authentication.principal.user.id")
    public Page<Bid> getBidsByUserId(Long userId, int pageStart, int pageSize) {
        return bidRepository.findBidByUserId(userId, PageRequest.of(pageStart, pageSize));
    }


    public void deleteBidById(Long bidId){
        SecurityContext securityContex = SecurityContextHolder.getContext();
        Authentication authentication = securityContex.getAuthentication();
        UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
        bidRepository.deleteBidById(bidId,userSecurity.getUser().getId());
    }

    }

