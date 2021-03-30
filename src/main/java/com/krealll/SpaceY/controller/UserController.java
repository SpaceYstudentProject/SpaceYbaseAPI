package com.krealll.SpaceY.controller;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.PasswordDto;
import com.krealll.SpaceY.model.dto.UserDto;
import com.krealll.SpaceY.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${spring.data.rest.base-path}users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    @PatchMapping("{id}/changePassword")
    public ResponseEntity changePassword(@PathVariable(name = "id") Integer id, @RequestBody PasswordDto password){
        Map<String,Object> response;
        response = userService.changePassword(password.getPassword(), id);
        if(response.containsKey("error")){
            return ResponseEntity.status((HttpStatus) response.get("error")).build();
        }
        return ResponseEntity.ok(response);
    }

}
