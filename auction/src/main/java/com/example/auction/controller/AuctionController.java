package com.example.auction.controller;

import com.example.auction.entity.Bid;
import com.example.auction.entity.Item;
import com.example.auction.service.BidService;
import com.example.auction.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuctionController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private BidService bidService;

    private static final Logger logger = LoggerFactory.getLogger(AuctionController.class);

    @GetMapping("/search")
    public String search(@RequestParam("query") String query , @RequestParam(value = "page" ,defaultValue = "0") Integer page , Model model){
        Page<Item> itemPage = itemService.searchItem(query, page, 12);
        model.addAttribute("itemPage", itemPage);
        model.addAttribute("query", query);
        return "auction";
    }

    @GetMapping("item/{id}")
    public String bid(@PathVariable("id") Long id , Model model){
        Item item = itemService.getItemById(id);
        Double highestBid = itemService.getHighestBidByItemId(id);
        if(item.getPrice() > highestBid) highestBid = item.getPrice();
        model.addAttribute("item" , item);
        model.addAttribute("highestBid" , highestBid);
        return "item_details.html";
    }

    @PostMapping("/bid")
    @ResponseBody
    public Map<String, Object> placeBid(@RequestParam("bidAmount") Double bidAmount,
                                        @RequestParam("itemId") Long itemId) {
        logger.info("Log example endpoint accessed");
            Map<String, Object> response = new HashMap<>();
            try {
                response = bidService.placeBid(itemId, bidAmount);
            } catch (ObjectOptimisticLockingFailureException e) {
                response.put("success", false);
                response.put("message", "Failed to place bid. Item has been updated by another transaction. Please try again.");
            }
            return response;
        }

    @GetMapping("/auctions")
    public String auction(@RequestParam(value = "page", defaultValue = "0") Integer page, Model model) {
        Page<Item> itemPage = itemService.getActiveItems(page, 12);
        model.addAttribute("itemPage", itemPage);
        return "auction";
    }

    }



