package com.example.auction.controller;

import com.example.auction.entity.Item;
import com.example.auction.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    ItemService itemService;

    @GetMapping("/search")
    public String searchBid(@RequestParam String searchText , Model model , @RequestParam(required = false , defaultValue = "0") Integer page){
        Page<Item> itemPage = itemService.findItemByName(searchText , page , 12 );
        model.addAttribute("itemPage" , itemPage);
        model.addAttribute("searchText" , searchText);
        return "search.html";
    }
}
