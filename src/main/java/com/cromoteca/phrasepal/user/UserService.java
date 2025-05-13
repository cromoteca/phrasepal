package com.cromoteca.phrasepal.user;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.annotation.security.PermitAll;

@BrowserCallable
@AnonymousAllowed
public class UserService {

    private final UserDetailsService userDetailsService;

    public UserService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .map(userDetailsService::loadUserByUsername)
                .map(User.class::cast);
    }

    @PermitAll
    public long getCurrentUserId() {
        return getCurrentUser().map(User::getId).orElseThrow();
    }
}
