package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.RegisterDTO;

import java.util.Map;

public interface AuthenticationService {

    Map<String,Object> resolveToken(User user, boolean rememberMe);

    boolean logoutAll(Integer userId);

    boolean logout(String token);

    Map<String, Object> registerUser(RegisterDTO registerDTO);
}
