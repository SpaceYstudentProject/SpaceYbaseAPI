package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.UserDto;
import com.krealll.SpaceY.repository.RoleRepository;
import com.krealll.SpaceY.repository.UserRepository;
import com.krealll.SpaceY.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public Map<String,Object> changePassword(String password, Integer id) {
        Map<String,Object> responce = new HashMap<>();
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            boolean result = userRepository.changePassword(password,id) > 0;
            if(!result){
                responce.put("error", HttpStatus.BAD_REQUEST);
                return responce;
            }
            Optional<User> changedUserOptional = userRepository.findById(id);
            if(changedUserOptional.isPresent()){
                responce.put("user", UserDto.fromUser(changedUserOptional.get()));
                return responce;
            } else {
                responce.put("error", HttpStatus.BAD_REQUEST);
                return responce;
            }
        } else {
            responce.put("error",HttpStatus.NOT_FOUND);
            return responce;
        }
    }
}
