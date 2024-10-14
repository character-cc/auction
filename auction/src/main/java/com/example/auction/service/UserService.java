package com.example.auction.service;

import com.example.auction.entity.User;
import com.example.auction.entity.UserSecurity;
import com.example.auction.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Autowired
    private SecurityContextHolderStrategy securityContextHolderStrategy;

    public boolean processSignup(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.findUserByUserName(username);
        if (user != null) {
            return true;
        }
        user = new User();
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        UserSecurity userSecurity = new UserSecurity(user);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userSecurity, null, userSecurity.getAuthorities()));
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        return false;
    }
}
