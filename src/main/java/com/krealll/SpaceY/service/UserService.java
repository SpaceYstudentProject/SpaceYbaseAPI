package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.User;

import java.util.List;

public interface UserService {


    List<User> findAll();

    User findByLogin( String login);

    User findById(Integer id);

    void delete(Integer id);
}
