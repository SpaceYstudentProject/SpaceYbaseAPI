package com.krealll.SpaceY.controller;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.UserDto;
import com.krealll.SpaceY.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/spacey/api/user/")
public class UserInfoRestControllerV1 {
//
    private final UserService userService;

    @Autowired
    public UserInfoRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "id") Long id  ){
        User user = userService.findById(id);
        if(user == null){
            log.warn("User with id - " + id + " was not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDto userDto = UserDto.fromUser(user);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
}
