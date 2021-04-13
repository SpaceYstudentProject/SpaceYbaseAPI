package com.krealll.SpaceY.service;

import com.krealll.SpaceY.controller.parameters.RequestParameters;
import com.krealll.SpaceY.controller.parameters.ResponseParameters;
import com.krealll.SpaceY.model.Permission;
import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.UserDTO;
import com.krealll.SpaceY.model.dto.UserQueryDTO;
import com.krealll.SpaceY.model.type.UserStatus;
import com.krealll.SpaceY.repository.UserRepository;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@PrepareForTest(UserServiceImpl.class)
public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;
    private ReflectionUtils reflectionUtils;
    private User user;
    private UserDTO userDTO;
    private Optional<User> optionalUser;
    private Map<String, Object> fields;
    private UserQueryDTO userQueryDTO;
    private QueryValidator queryValidator;

    @BeforeMethod
    public void setUp() {
        userRepository = mock(UserRepository.class);
        reflectionUtils = mock(ReflectionUtils.class);
        queryValidator = mock(QueryValidator.class);
        userService = new UserServiceImpl(userRepository);

        Set<Permission> permissions = new HashSet<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        role.setPermissions(permissions);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user = new User();
        user.setLogin("Daetwen");
        user.setEmail("123vlad.@gmail.com");
        user.setPassword(encoder.encode("123456"));
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(roles);
        user.setId(0);

        userDTO = UserDTO.fromUser(user);

        optionalUser = Optional.of(user);

        fields = new HashMap<>();
    }

    @Test
    public void updateUserTrue() {
        Map<String, Object> expected = new HashMap<>();
        userDTO.setEmail("123@mail.ru");
        fields.put("email", "123@mail.ru");
        expected.put(ResponseParameters.USER, userDTO);
        when(userRepository.findById(anyInt())).thenReturn(optionalUser);
        when(userRepository.save(any(User.class))).thenReturn(user);
        Map<String, Object> actual = userService.updateUser(1, fields);
        assertEquals(expected, actual);
    }

    @Test
    public void updateUserFalseOne() {
        Map<String, Object> expected = new HashMap<>();
        fields.put(RequestParameters.ID, 1);
        expected.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
        when(userRepository.findById(anyInt())).thenReturn(optionalUser);
        Map<String, Object> actual = userService.updateUser(1, fields);
        assertEquals(expected, actual);
    }

    @Test
    public void updateUserFalseTwo() {
        Map<String, Object> expected = new HashMap<>();
        expected.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
        when(userRepository.findById(anyInt())).thenReturn(optionalUser);
        fields.put("ERRORfdjkfdhk",123);
        Map<String, Object> actual = userService.updateUser(1, fields);
        assertEquals(expected, actual);
    }

    @Test
    public void updateUserFalseThree() {
        Map<String, Object> expected = new HashMap<>();
        expected.put(ResponseParameters.ERROR, HttpStatus.NOT_FOUND);
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        fields.put("id", RequestParameters.ID);
        Map<String, Object> actual = userService.updateUser(1, fields);
        assertEquals(expected, actual);
    }

//    @Test
//    public void queryFindFalseOne() {
//        Map<String, Object> expected = new HashMap<>();
//        expected.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
//        when(queryValidator.validateQuery(anyMap(),anyMap())).thenReturn(false);
//        Map<String, Object> actual = userService.queryFind(userQueryDTO);
//        assertEquals(expected, actual);
//    }
}
