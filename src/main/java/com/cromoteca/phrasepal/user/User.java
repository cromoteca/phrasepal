package com.cromoteca.phrasepal.user;

import com.cromoteca.phrasepal.languages.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "spoken_language_id", nullable = false)
    private Language spokenLanguage;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "studied_language_id", nullable = false)
    private Language studiedLanguage;

    public User() {}

    public User(String email) {
        this.email = email;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Language getSpokenLanguage() {
        return spokenLanguage;
    }

    public void setSpokenLanguage(Language spokenLanguage) {
        this.spokenLanguage = spokenLanguage;
    }

    public Language getStudiedLanguage() {
        return studiedLanguage;
    }

    public void setStudiedLanguage(Language studiedLanguage) {
        this.studiedLanguage = studiedLanguage;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // No roles for now
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
