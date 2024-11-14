package com.example.auction.controller;

import com.example.auction.entity.Bid;
import com.example.auction.entity.UserSecurity;
import com.example.auction.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BidController {

    @Autowired
    BidService bidService;

    @GetMapping("bids/{id}")
    public String bids(@PathVariable("id") Long userId , @RequestParam(value = "page", defaultValue = "0") Integer page , Model model){

        Page<Bid> bids = bidService.getBidsByUserId(userId, page,2);
        model.addAttribute("bids" ,bids);
        return "bids";
    }

    @PostMapping("/bids/{bidId}/delete")
    public String deleteBid(@PathVariable Long bidId, RedirectAttributes redirectAttributes , Authentication authentication) {
        try {
            bidService.deleteBidById(bidId);
            redirectAttributes.addFlashAttribute("successMessage", "Bid successfully deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete the bid.");
        }
        return "redirect:/bids/" +  ((UserSecurity) authentication.getPrincipal()).getUser().getId(); // Adjust the redirect URL as per your routing
    }

    @GetMapping("/bids/item/{id}")
    public String viewBidsForItem(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, Model model) {
        Page<Bid> bidPage = bidService.getBidsByItemId(id, page, 10);
        model.addAttribute("bidPage", bidPage);
        model.addAttribute("itemId", id);
        return "item-bids";
    }

}
