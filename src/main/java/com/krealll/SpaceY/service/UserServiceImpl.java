package com.krealll.SpaceY.service;

import com.krealll.SpaceY.controller.parameters.RequestParameters;
import com.krealll.SpaceY.controller.parameters.ResponseParameters;
import com.krealll.SpaceY.model.dto.DTOParameters;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.UserDTO;
import com.krealll.SpaceY.model.dto.UserQueryDTO;
import com.krealll.SpaceY.model.type.UserStatus;
import com.krealll.SpaceY.repository.UserRepository;
import com.krealll.SpaceY.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User findById(Integer id) {
        User result = userRepository.findById(id).orElse(null);
        if (result == null) {
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
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            if (fields.containsKey(RequestParameters.ID) ||
                    fields.containsKey(RequestParameters.ROLES) ||
                    fields.containsKey(RequestParameters.TOKENS)) {
                response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
                return response;
                //TODO add validation here instead
            }
            for (Map.Entry<String, Object> mapEntry : fields.entrySet()) {
                Field field = ReflectionUtils.findField(User.class, mapEntry.getKey());
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, userOptional.get(), mapEntry.getValue());
                } else {
                    response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
                    return response;
                }
            }
            User updatedUser = userRepository.save(userOptional.get());
            response.put(ResponseParameters.USER, UserDTO.fromUser(updatedUser));
            return response;
        } else {
            response.put(ResponseParameters.ERROR, HttpStatus.NOT_FOUND);
            return response;
        }
    }

    @Override
    public Map<String, Object> queryFind(UserQueryDTO userQueryDto) {
        Map<String, Object> response = new HashMap<>();
        List<User> users;
        Map<String, Object> query = userQueryDto.getQuery();
        Map<String, Object> options = userQueryDto.getOptions();
        boolean validateResult = QueryValidator.validateQuery(query,options);
        if (!validateResult) {
            response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
            return response;
        }

        if (options.isEmpty()) {
            if (query.containsKey(DTOParameters.PAGE_NUM) && query.containsKey(DTOParameters.PAGE_SIZE)) {
                Pageable findUsersPage = PageRequest.of(Integer.parseInt((String) query.get(DTOParameters.PAGE_NUM)),
                        Integer.parseInt((String) query.get(DTOParameters.PAGE_SIZE)));
                users = userRepository.findAll(findUsersPage).getContent();
                response.put(ResponseParameters.USERS, UserDTO.fromUsers(users));
                return response;
            } else if (query.containsKey(DTOParameters.PAGE_SIZE)) {
                Pageable findUsersPage = PageRequest.of(0,
                        Integer.parseInt((String) query.get(DTOParameters.PAGE_SIZE)));
                users = userRepository.findAll(findUsersPage).getContent();
                response.put(ResponseParameters.USERS, UserDTO.fromUsers(users));
                return response;
            } else {
                response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
                return response;
            }
        } else if (!query.isEmpty()) {
            if (options.containsKey(DTOParameters.USERNAME)) {
                if (options.containsKey(DTOParameters.STATUS)) {
                    response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
                    return response;
                }
                users = userRepository.findAllByLogin((String) options.get(DTOParameters.USERNAME));
                response.put(ResponseParameters.USERS, UserDTO.fromUsers(users));
                return response;
            } else if (options.containsKey(DTOParameters.STATUS)) {
                if (query.containsKey(DTOParameters.PAGE_SIZE) && query.containsKey(DTOParameters.PAGE_NUM)) {
                    Pageable findUsersPage = PageRequest.of(Integer.parseInt((String) query.get(DTOParameters.PAGE_NUM)),
                            Integer.parseInt((String) query.get(DTOParameters.PAGE_SIZE)));
                    users = userRepository
                            .findAllByStatus(UserStatus.valueOf((String) options.get(DTOParameters.STATUS)), findUsersPage);
                    response.put(ResponseParameters.USERS, UserDTO.fromUsers(users));
                    return response;
                } else if (query.containsKey(DTOParameters.PAGE_SIZE)) {
                    Pageable findUsersPage = PageRequest.of(0, Integer.parseInt((String) query.get(DTOParameters.PAGE_SIZE)));
                    users = userRepository
                            .findAllByStatus(UserStatus.valueOf((String) options.get(DTOParameters.STATUS)), findUsersPage);
                    response.put(ResponseParameters.USERS, UserDTO.fromUsers(users));
                    return response;
                } else {
                    users = userRepository.findAllByStatus(UserStatus.valueOf((String) options.get(DTOParameters.STATUS)));
                    response.put(ResponseParameters.USERS, UserDTO.fromUsers(users));
                    return response;
                }
            }
        } else {
            response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
            return response;
        }
        response.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
        return response;
    }


}
