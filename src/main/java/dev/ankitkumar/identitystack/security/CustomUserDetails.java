package dev.ankitkumar.identitystack.security;

import dev.ankitkumar.identitystack.entity.User;
import lombok.AllArgsConstructor;


import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
@Lazy
public class CustomUserDetails implements UserDetails {


    private User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public  String getUsername() {
        return user.getUsername();
    }
}
