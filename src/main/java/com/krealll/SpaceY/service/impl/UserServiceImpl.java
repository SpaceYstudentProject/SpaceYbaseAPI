package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.repository.RoleRepository;
import com.krealll.SpaceY.repository.UserRepository;
import com.krealll.SpaceY.service.UserService;
import liquibase.pro.packaged.A;
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
    private BCryptPasswordEncoder encoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User register(User user) {
        Role userRole = roleRepository.findByName("USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        User registered = userRepository.save(user);

        return registered;
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
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);
        if(result == null){
            log.warn("No such user: {}", id);
            return null;
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
