package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.repository.RoleRepository;
import com.krealll.SpaceY.repository.UserRepository;
import com.krealll.SpaceY.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,  RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> result = userRepository.findAll();
        return result;
    }

    @Override
    public User findByLogin(String login) {
        User result = userRepository.findByLogin(login);
        return result;
    }

    @Override
    public User findById(Integer id) {
        User result = userRepository.findById(id).orElse(null);
        if(result == null){
            log.warn("No such user: {}", id);
            return null;
        }
        return result;
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
