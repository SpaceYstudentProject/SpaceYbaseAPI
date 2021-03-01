package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> findAll();

    User findByLogin( String login);

    User findById(Long id);

    void delete(Long id);
}
