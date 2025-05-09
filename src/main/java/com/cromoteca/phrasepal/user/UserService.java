package com.cromoteca.phrasepal.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
public class UserService {

    private final UserDetailsService userDetailsService;

    public UserService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
            if (userDetails != null) {
                return (User) userDetails;
            }
        }
        return null;
    }
}
