package com.krealll.SpaceY.security;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.security.jwt.JwtUser;
import com.krealll.SpaceY.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService  implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByLogin(s);
        if(user == null){
            throw new UsernameNotFoundException("User with login :" + s + " not found");
        }
        JwtUser jwtUser = TokenInfoFactory.create(user);

        log.info("IN loadUserByUsername user with login: {} was loaded", user.getLogin());
        return jwtUser;
    }
}
