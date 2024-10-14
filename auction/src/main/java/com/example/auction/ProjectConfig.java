package com.example.auction;

import com.example.auction.service.CustomAuthenticationProvider;
import com.example.auction.service.CustomUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(c -> {
            c.requestMatchers("/item/{id}", "/bids/{id}", "/bids/{bidId}/delete").authenticated();
            c.anyRequest().permitAll();
        });

        httpSecurity.formLogin(c -> {
            c.loginPage("/login").permitAll();
            c.successHandler((request, response, authentication) -> {
                SavedRequest savedRequest = (SavedRequest) request.getSession()
                        .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                if (savedRequest == null) {
                    response.sendRedirect("/home");
                } else {
                    response.sendRedirect(savedRequest.getRedirectUrl());
                }
            });

            c.failureUrl("/login?error=true");
        });

        httpSecurity.logout(c -> c
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
        );
        httpSecurity.userDetailsService(userDetailsService());
        httpSecurity.authenticationProvider(authenticationProvider());

        return httpSecurity.build();
    }


    @Bean
    UserDetailsService userDetailsService(){
        return new CustomUserDetailService();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        return new CustomAuthenticationProvider(passwordEncoder(),userDetailsService());
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityContextHolderStrategy securityContextHolderStrategy() {
        return SecurityContextHolder.getContextHolderStrategy();
    }
}
