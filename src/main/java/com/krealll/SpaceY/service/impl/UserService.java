package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.UserQueryDTO;

import java.util.List;
import java.util.Map;

public interface UserService {


    List<User> findAll();

    User findByLogin(String login);

    User findById(Integer id);

    void delete(Integer id);

    Map<String,Object> updateUser(Integer id, Map<String, Object> fields);

    Map<String,Object> queryFind(UserQueryDTO userQueryDto);


}
