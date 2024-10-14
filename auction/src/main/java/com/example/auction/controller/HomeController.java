package com.example.auction.controller;

import com.example.auction.entity.Item;
import com.example.auction.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/home")
    public String home(Model model){
        List<Item> items = itemService.getActiveItems(0,3).getContent();
        model.addAttribute("items" ,items);
        return "home.html";
    }



}
