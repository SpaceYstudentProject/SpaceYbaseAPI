package com.krealll.SpaceY.model.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class LoginDTO {

    private  String username;
    private String password;
    private boolean rememberMe;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDTO loginDTO = (LoginDTO) o;
        return rememberMe == loginDTO.rememberMe && Objects.equals(username, loginDTO.username) && Objects.equals(password, loginDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, rememberMe);
    }

}
