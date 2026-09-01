package dev.ankitkumar.identitystack.security;

import dev.ankitkumar.identitystack.entity.User;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
@Lazy
public class CustomUserDetails implements UserDetails {


    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).toList();

    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public long getId() {

        return user.getId();
    }

    public int getTokenVersion() {
        return user.getTokenVersion();
    }
}
