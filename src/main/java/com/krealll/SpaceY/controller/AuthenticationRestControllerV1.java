package com.krealll.SpaceY.controller;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.AuthenticationRequestDto;
import com.krealll.SpaceY.security.jwt.JwtTokenProvider;
import com.krealll.SpaceY.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping( value = "/spacey/api/auth/")
public class AuthenticationRestControllerV1 {


    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto){

            String login = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login,requestDto.getPassword()));
            User user = userService.findByLogin(login);
            if(user == null){
                throw new UsernameNotFoundException("User with login - " + login + " now found");
            }
            String token = jwtTokenProvider.createToken(login, user.getRoles());
            Map<Object,Object> response = new HashMap<>();
            response.put("username", login);
            response.put("token" , token);
            return ResponseEntity.ok(response);


    }
}
