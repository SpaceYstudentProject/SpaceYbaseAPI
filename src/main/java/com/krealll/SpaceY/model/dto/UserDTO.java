package com.krealll.SpaceY.model.dto;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.type.UserStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    private Integer id;
    private String login;
    private String email;
    private UserStatus userStatus;

    public User toUser(){
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setStatus(userStatus);
        return user;
    }

    public static List<UserDTO> fromUsers(List<User> users){
        List<UserDTO> userDtos = new ArrayList<>();
        if(users != null){
            for (User user: users) {
                userDtos.add(UserDTO.fromUser(user));
            }
        }
        return userDtos;
    }

    public static UserDTO fromUser(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setUserStatus(user.getStatus());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        return userDto;
    }
}
