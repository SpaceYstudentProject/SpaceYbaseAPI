package com.krealll.SpaceY.controller;

import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.UpdateDto;
import com.krealll.SpaceY.model.dto.UserDto;
import com.krealll.SpaceY.model.dto.UserQueryDto;
import com.krealll.SpaceY.model.type.UserStatus;
import com.krealll.SpaceY.repository.UserRepository;
import com.krealll.SpaceY.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("${spring.data.rest.base-path}users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "id") Integer id  ){
        User user = userService.findById(id);
        if(user == null){
            log.warn("User with id - " + id + " was not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDto userDto = UserDto.fromUser(user);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getUsers(){
        List<UserDto> users;
        users = UserDto.fromUsers(userService.findAll());
        if(users.size() == 0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable(name = "id") Integer id, @RequestBody Map<String, Object> fields){
        Map<String,Object> response;
        response = userService.updateUser(id,fields);
        if(response.containsKey("error")){
            return ResponseEntity.status((HttpStatus) response.get("error")).build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/query")
    public ResponseEntity queryUsers(@RequestBody UserQueryDto userQueryDto){
        if(userQueryDto.getQuery() == null || userQueryDto.getOptions() == null){
            return ResponseEntity.badRequest().build();
        }
        Map<String,Object> response;
        response = userService.queryFind(userQueryDto);
        if(response.containsKey("error")){
            return ResponseEntity.status((HttpStatus) response.get("error")).build();
        }
        return ResponseEntity.ok(response);
    }
}
