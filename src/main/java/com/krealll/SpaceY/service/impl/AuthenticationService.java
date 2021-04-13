package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.RefreshToken;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.RefreshDTO;
import com.krealll.SpaceY.model.dto.RegisterDTO;

import java.util.Map;

public interface AuthenticationService {

    Map<String,Object> resolveToken(User user, boolean rememberMe);

    boolean logoutAll(Integer userId);

    boolean logout(String token);

    Map<String, Object> registerUser(RegisterDTO registerDTO);

    Map<String, Object> refreshToken(RefreshDTO refreshDTO);

}
