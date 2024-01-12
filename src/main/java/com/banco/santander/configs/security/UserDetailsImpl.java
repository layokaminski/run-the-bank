package com.banco.santander.configs.security;

import com.banco.santander.entities.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final UUID id;
    private final String name;
    private final String document;
    @JsonIgnore
    private final String password;

    public static UserDetailsImpl build(Customer userRepository) {

        return new UserDetailsImpl(
                userRepository.getId(),
                userRepository.getName(),
                userRepository.getDocument(),
                userRepository.getPassword()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    @Override
    public String getUsername() {
        return this.document;
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