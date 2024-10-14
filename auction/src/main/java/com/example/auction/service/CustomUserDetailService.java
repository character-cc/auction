package com.example.auction.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.auction.entity.User;
import com.example.auction.entity.UserSecurity;
import com.example.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public class CustomUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    @Autowired
    UserRepository userRepository;

    public CustomUserDetailService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username);
        if(user == null) throw new UsernameNotFoundException("User not found with username: " + username);
        return new UserSecurity(user);
    }
}
