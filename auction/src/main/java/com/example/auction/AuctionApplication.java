package com.example.auction;

import com.example.auction.entity.Address;
import com.example.auction.entity.Bid;
import com.example.auction.entity.Item;
import com.example.auction.entity.User;
import com.example.auction.repository.ItemRepository;
import com.example.auction.repository.UserRepository;
//import com.example.auction.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@SpringBootApplication
public class AuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class, args);
	}


	@Bean
	public ApplicationRunner configure(UserRepository userRepository , ItemRepository itemRepository ) {
		return env ->
		{
			Set<Address> addressSet = new HashSet<Address>();
			Address address = new Address("a","a","a","a","a");
			addressSet.add(address);
			List<Item> items = new ArrayList<Item>();
			User user = new User("test","test","jsajd",addressSet);

			Item item1 = new Item("Smartphone", "Latest model smartphone", 1000.0, "/images/ip.png",
					LocalDateTime.of(2024, 10, 5, 10, 30),
					LocalDateTime.of(2025, 10, 10, 18, 0), user);

			Item item2 = new Item("Smartphone1", "Latest model smartphone", 1000.0, "/images/ip.png",
					LocalDateTime.of(2024, 10, 5, 10, 30),
					LocalDateTime.of(2025, 10, 10, 18, 0), user);

			Item item3 = new Item("Smartphone2", "Latest model smartphone", 1000.0, "/images/ip.png",
					LocalDateTime.of(2024, 10, 5, 10, 30),
					LocalDateTime.of(2025, 10, 10, 18, 0), user);
			user.getItems().add(item1);
			user.getItems().add(item2);
			user.getItems().add(item3);
			Bid bid1 = new Bid(user,11111.0,item1,LocalDateTime.now());
			Bid bid2 = new Bid(user,1223.0,item2,LocalDateTime.now());
			Bid bid3 = new Bid(user,13223.0,item3,LocalDateTime.now());
			user.getBids().add(bid1);
			user.getBids().add(bid2);
			user.getBids().add(bid3);

			User user1 = new User("test1","test","jsajd",addressSet);
			Bid bid5 = new Bid(user1,1.0,item1,LocalDateTime.now());
			Bid bid4 = new Bid(user1,1.0,item2,LocalDateTime.now());
			user1.getBids().add(bid5);
			user1.getBids().add(bid4);
			userRepository.save(user);
			userRepository.save(user1);

			//	userRepository.save(user2);
//			List<User> user1 =  userRepository.findById(user.getId()).stream().toList();
//		//	List<Address> addressList= user.getAddresses().stream().toList();
//			System.out.println(user1.get(0).getAddresses().stream().toList().get(0).getCity());
		//	userRepository.findAll().forEach(System.out::println);
//
//			redisService.saveData("testKey", "Hello, Redis!");
//			System.out.println((String) redisService.getData("testKey"));
		};
	}
}
