package com.example.auction.controller;

import com.example.auction.entity.Item;
import com.example.auction.entity.User;
import com.example.auction.entity.UserSecurity;
import com.example.auction.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class ItemController {

    @Autowired
    ItemService itemService;

    @GetMapping("/items/create-item")
    public String showCreateForm(Model model){
        model.addAttribute("item" , new Item());
        return "create-item";
    }

    @PostMapping("/items/create-item")
    public String createItem(@ModelAttribute("item") Item item , @RequestParam("image") MultipartFile image , Authentication authentication){
        UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
        item.setUser(userSecurity.getUser());
        if (!image.isEmpty()) {
            try {
                String imageUrl = saveImage(image);
                item.setImageUrl(imageUrl);
            }
            catch (IOException e){

            }
        }
        itemService.saveItem(item);
        return "redirect:/my-items";
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "src/main/resources/static/images/";
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/images/" + fileName;
    }

    @GetMapping("/my-items")
    public String getMyItems(Model model,Authentication authentication, @RequestParam(defaultValue = "0") int page) {
        User currentUser = ((UserSecurity) authentication.getPrincipal()).getUser();
        Page<Item> itemPage = itemService.findItemsByUserId(currentUser.getId(), page, 12);
        model.addAttribute("itemPage", itemPage);
        return "my-items";
    }

}
