package com.cromoteca.phrasepal.words;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.cromoteca.phrasepal.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "words", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"word", "user_id"})})
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String word;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private int failedUsages;

    private int successfulUsages;

    public Word() {
        // Default constructor required by JPA
    }

    public Word(String word, User user) {
        this.word = word;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getFailedUsages() {
        return failedUsages;
    }

    public void setFailedUsages(int failedUsages) {
        this.failedUsages = failedUsages;
    }

    public int getSuccessfulUsages() {
        return successfulUsages;
    }

    public void setSuccessfulUsages(int successfulUsages) {
        this.successfulUsages = successfulUsages;
    }

}
