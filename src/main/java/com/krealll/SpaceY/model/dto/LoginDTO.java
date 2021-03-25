package com.krealll.SpaceY.model.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private  String username;
    private String password;
    private boolean rememberMe;

}
