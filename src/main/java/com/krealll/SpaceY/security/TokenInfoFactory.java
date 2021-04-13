package com.krealll.SpaceY.security;

import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.type.UserStatus;
import com.krealll.SpaceY.security.jwt.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class TokenInfoFactory {

    private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";

    private TokenInfoFactory() {
    }

    public static JwtUser create(User user){
        return new JwtUser(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus().equals(UserStatus.ACTIVE),
                mapToGrantedAuthorities(user.getRoles()));
    }

    public static Set<GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles){
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_AUTHORITY_PREFIX + role.getName()))
                .collect(Collectors.toSet()));
        authorities.addAll(roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet()));
        return authorities;
    }
}
