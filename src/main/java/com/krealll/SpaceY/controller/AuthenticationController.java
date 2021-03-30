package com.krealll.SpaceY.controller;

import com.krealll.SpaceY.model.User;

import com.krealll.SpaceY.model.dto.LoginDTO;
import com.krealll.SpaceY.model.dto.RefreshDTO;
import com.krealll.SpaceY.model.dto.RegisterDTO;
import com.krealll.SpaceY.security.TokenProvider;
import com.krealll.SpaceY.service.AuthenticationService;
import com.krealll.SpaceY.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping( value =  "${spring.data.rest.base-path}auth/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));
        User user = userService.findByLogin(loginDTO.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("User with login - " + loginDTO.getUsername() + " now found");
        } else {
            Map<String,Object> response;
            response = authenticationService.resolveToken(user, loginDTO.isRememberMe());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("logout/{id}")
    public ResponseEntity logout(@PathVariable(name = "id")Integer id){
        boolean result;
        result = authenticationService.logoutAll(id);
        if(result){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("logout/token/{value}")
    public ResponseEntity logout(@PathVariable(name = "value")String value){
        boolean result;
        result = authenticationService.logout(value);
        if(result){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterDTO registerDTO){
        Map<String, Object> response;
        response = authenticationService.registerUser(registerDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("refresh")
    public ResponseEntity refresh(@RequestBody RefreshDTO refreshDTO){
        Map<String,Object> response;
        response = authenticationService.refreshToken(refreshDTO);
        if(response.containsKey("error")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(response);
    }

}
