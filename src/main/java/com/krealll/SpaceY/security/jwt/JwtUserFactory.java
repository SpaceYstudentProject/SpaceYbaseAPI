package com.krealll.SpaceY.security.jwt;

import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user){
        return new JwtUser(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus().equals(UserStatus.ACTIVE),
                null);
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles){
        return roles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
