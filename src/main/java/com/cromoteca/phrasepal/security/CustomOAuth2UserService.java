package com.cromoteca.phrasepal.security;

import com.cromoteca.phrasepal.languages.Language;
import com.cromoteca.phrasepal.languages.LanguageRepository;
import com.cromoteca.phrasepal.user.User;
import com.cromoteca.phrasepal.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        if (email != null) {
            userRepository.findByEmail(email).orElseGet(() -> {
                User user = new User(email);
                // Set spokenLanguage to English and studiedLanguage to French by code
                Language spoken = languageRepository
                        .findByCode("en-US")
                        .orElseThrow(() -> new IllegalStateException("English language not found"));
                Language studied = languageRepository
                        .findByCode("fr-FR")
                        .orElseThrow(() -> new IllegalStateException("French language not found"));
                user.setSpokenLanguage(spoken);
                user.setStudiedLanguage(studied);
                return userRepository.save(user);
            });
        }
        return oAuth2User;
    }
}
