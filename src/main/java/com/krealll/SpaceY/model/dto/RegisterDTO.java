package com.krealll.SpaceY.model.dto;

import com.krealll.SpaceY.model.Permission;
import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.type.UserStatus;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class RegisterDTO extends LoginDTO{

    private String email;

    public User toUser(){
        Set<Permission> permissions = new HashSet<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        role.setPermissions(permissions);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User();
        user.setLogin(this.getUsername());
        user.setEmail(this.getEmail());
        user.setPassword(encoder.encode(this.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(roles);
        return user;
    }
}
