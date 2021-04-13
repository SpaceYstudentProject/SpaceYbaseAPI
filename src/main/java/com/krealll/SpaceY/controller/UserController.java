package com.krealll.SpaceY.controller;

import com.krealll.SpaceY.controller.parameters.ResponseParameters;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.UserDTO;
import com.krealll.SpaceY.model.dto.UserQueryDTO;
import com.krealll.SpaceY.service.impl.UserService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "find user by id",
            notes = "Return HttpStatus.NOT_FOUND or user and HttpStatus.OK.")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") Integer id  ){
        User user = userService.findById(id);
        if(user == null){
            log.warn("User with id - " + id + " was not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDTO userDto = UserDTO.fromUser(user);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @GetMapping("/")
    @ApiOperation(value = "find all users",
            notes = "Return HttpStatus.NO_CONTENT or list of all user and HttpStatus.OK.")
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<UserDTO> users;
        users = UserDTO.fromUsers(userService.findAll());
        if(users.size() == 0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "find all users",
            notes = "If the update failed, then return error. If all ok, then upade person and return ok.")
    public ResponseEntity updateUser(@PathVariable(name = "id") Integer id, @RequestBody Map<String, Object> fields){
        Map<String,Object> response;
        response = userService.updateUser(id,fields);
        if(response.containsKey(ResponseParameters.ERROR)){
            return ResponseEntity.status((HttpStatus) response.get(ResponseParameters.ERROR)).build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/query")
    @ApiOperation(value = "get users by some parameters",
            notes = "If the finding failed, then return error. If all ok, then return persons and ResponseEntity.ok.")
    public ResponseEntity queryUsers(@RequestBody UserQueryDTO userQueryDto){
        if(userQueryDto.getQuery() == null || userQueryDto.getOptions() == null){
            return ResponseEntity.badRequest().build();
        }
        Map<String,Object> response;
        response = userService.queryFind(userQueryDto);
        if(response.containsKey(ResponseParameters.ERROR)){
            return ResponseEntity.status((HttpStatus) response.get(ResponseParameters.ERROR)).build();
        }
        return ResponseEntity.ok(response);
    }
}
