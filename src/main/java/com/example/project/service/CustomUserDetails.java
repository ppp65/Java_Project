package com.example.project.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final String userId;
    private final String username;
    private final String password;
    private final int points;
    private final String nicknameColor;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String userId, String username, String password, int points, String nicknameColor, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.points = points;
        this.nicknameColor = nicknameColor;
        this.authorities = authorities;
    }

    public String getUserId() {
        return userId;
    }

    public int getPoints() {
        return points;
    }

    public String getNicknameColor() {
        return nicknameColor;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
