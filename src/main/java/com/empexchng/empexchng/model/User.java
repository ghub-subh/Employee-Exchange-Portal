package com.empexchng.empexchng.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails { // <-- Implement UserDetails

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    // Make password nullable, as OAuth2 users won't have one
    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    // --- NEW FIELD FOR ADMIN APPROVAL ---
    @Column(name = "is_approved", nullable = false)
    private boolean isApproved = false; // Default to false

    // --- UserDetails METHODS ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security needs the "ROLE_" prefix
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        // We use email as the username
        return this.email;
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
        // This is the key! A user is only "enabled" if an admin has approved them.
        return this.isApproved;
    }
}