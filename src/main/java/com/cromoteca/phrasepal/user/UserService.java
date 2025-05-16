package com.cromoteca.phrasepal.user;

import com.cromoteca.phrasepal.languages.Language;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudRepositoryService;
import jakarta.annotation.security.PermitAll;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

@BrowserCallable
@AnonymousAllowed
public class UserService extends CrudRepositoryService<User, Long, UserRepository> {

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

    @NonNull
    public User updateStudiedLanguage(long userId, @NonNull Language language) {
        var user = getRepository().findById(userId).orElseThrow();
        user.setStudiedLanguage(language);
        return getRepository().save(user);
    }

    @NonNull
    public User updateSpokenLanguage(long userId, @NonNull Language language) {
        var user = getRepository().findById(userId).orElseThrow();
        user.setSpokenLanguage(language);
        return getRepository().save(user);
    }
}
