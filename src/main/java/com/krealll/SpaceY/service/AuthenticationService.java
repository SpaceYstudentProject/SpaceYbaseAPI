package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.User;

import java.util.Map;

public interface AuthenticationService {

    Map<String,Object> resolveToken(User user, boolean rememberMe);

}
