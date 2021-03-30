package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {


    List<User> findAll();

    User findByLogin( String login);

    User findById(Integer id);

    void delete(Integer id);

    Map<String,Object> changePassword(String password, Integer id);
}
