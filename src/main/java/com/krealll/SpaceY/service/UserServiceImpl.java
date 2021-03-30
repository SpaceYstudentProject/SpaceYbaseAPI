package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.UpdateDto;
import com.krealll.SpaceY.model.dto.UserDto;
import com.krealll.SpaceY.repository.UserRepository;
import com.krealll.SpaceY.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public Map<String, Object> updateUser(Integer id, Map<String, Object> fields) {
        Map<String,Object> responce = new HashMap<>();
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            if(fields.containsKey("id")||fields.containsKey("roles")||fields.containsKey("tokens")){
                responce.put("error", HttpStatus.BAD_REQUEST);
                return responce;
            }
            for (Map.Entry<String,Object> mapEntry:fields.entrySet()) {
                Field field = ReflectionUtils.findField(User.class,mapEntry.getKey());
                if(field != null){
                    if(!field.getName().equals("id")&&
                            !field.getName().equals("roles")&&
                            !field.getName().equals("tokens")){
                        field.setAccessible(true);
                        ReflectionUtils.setField(field,userOptional.get(), mapEntry.getValue());
                    } else {
                        responce.put("error", HttpStatus.BAD_REQUEST);
                        return responce;
                    }
                } else {
                    responce.put("error", HttpStatus.BAD_REQUEST);
                    return responce;
                }
            }
            User updatedUser = userRepository.save(userOptional.get());
            responce.put("user", UserDto.fromUser(updatedUser));
            return responce;
        } else {
            responce.put("error",HttpStatus.NOT_FOUND);
            return responce;
        }
    }

}
