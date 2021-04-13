package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.Permission;
import com.krealll.SpaceY.model.RefreshToken;
import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.RefreshDTO;
import com.krealll.SpaceY.model.dto.RegisterDTO;
import com.krealll.SpaceY.model.type.UserStatus;
import com.krealll.SpaceY.repository.TokenRepository;
import com.krealll.SpaceY.repository.UserRepository;
import com.krealll.SpaceY.security.TokenProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

@PrepareForTest(AuthenticationServiceImpl.class)
public class AuthenticationServiceImplTest {

    private AuthenticationServiceImpl authenticationService;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private TokenProvider tokenProvider;
    private BCryptPasswordEncoder encoder;
    private User user;
    private RegisterDTO registerDTO;
    private RefreshDTO refreshDTO;
    private Optional<RefreshToken> optionalRefreshToken;
    private RefreshToken refreshToken;
    private RandomStringUtils randomStringUtils;
    private Integer refTokenLength;

    @BeforeMethod
    public void setUp() {
        refTokenLength = 64;
        tokenRepository = mock(TokenRepository.class);
        userRepository = mock(UserRepository.class);
        tokenProvider = mock(TokenProvider.class);
        encoder = mock(BCryptPasswordEncoder.class);
        randomStringUtils = mock(RandomStringUtils.class);
        authenticationService = new AuthenticationServiceImpl(encoder, userRepository, tokenRepository, tokenProvider);

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
        registerDTO = new RegisterDTO();
        registerDTO.setUsername("Daetwen");
        registerDTO.setEmail("123vlad.@gmail.com");
        registerDTO.setPassword(encoder.encode("123456"));
        registerDTO.setRememberMe(true);

        refreshToken = new RefreshToken();
        refreshToken.setId(1);
        refreshToken.setUsersId(user.getId());
        refreshToken.setExpiresIn(12L);
        refreshToken.setValue("Test refresh token");
        refreshToken.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        refreshDTO = new RefreshDTO();
        refreshDTO.setToken("Hello token");
        refreshDTO.setUsername("Daetwen");

        optionalRefreshToken = Optional.of(refreshToken);
    }

    @Test
    public void resolveTokenTrueOne() {
        Map<String, Object> expected = new HashMap<>();
        String refTokenValue = "refTokenValue";
        String accessToken = "Test token";
        authenticationService.setRefTokenTTL(90L);
        authenticationService.setRefTokenLength(64);
        refreshToken.setValue(refTokenValue);
        when(tokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);
        when(tokenProvider.createToken(user.getLogin(), user.getRoles())).thenReturn(accessToken);
        expected.put("username", user.getLogin());
        expected.put("jwt", accessToken);
        expected.put("ref" , refreshToken);
        Map<String, Object> actual = authenticationService.resolveToken(user, true);
        assertEquals(expected, actual);
    }

    @Test
    public void resolveTokenTrueTwo() {
        Map<String, Object> expected = new HashMap<>();
        String accessToken = "Test token";
        expected.put("username", user.getLogin());
        expected.put("jwt", accessToken);
        when(tokenProvider.createToken(user.getLogin(), user.getRoles())).thenReturn(accessToken);
        Map<String, Object> actual = authenticationService.resolveToken(user, false);
        assertEquals(expected, actual);
    }

    @Test
    public void logoutAllTrue() {
        when(tokenRepository.deleteByUsersId(anyInt())).thenReturn(1);
        boolean actual = authenticationService.logoutAll(1);
        assertTrue(actual);
    }

    @Test
    public void logoutAllFalse() {
        when(tokenRepository.deleteByUsersId(anyInt())).thenReturn(0);
        boolean actual = authenticationService.logoutAll(1);
        assertFalse(actual);
    }

    @Test
    public void logoutTrue() {
        when(tokenRepository.deleteByValue(anyString())).thenReturn(1);
        boolean actual = authenticationService.logout("Hello");
        assertTrue(actual);
    }

    @Test
    public void logoutFalse() {
        when(tokenRepository.deleteByValue(anyString())).thenReturn(0);
        boolean actual = authenticationService.logout("Hello");
        assertFalse(actual);
    }

    @Test
    public void registerUserTrueOne() {
        Map<String, Object> expected = new HashMap<>();
        String refTokenValue = "refTokenValue";
        String accessToken = tokenProvider.createToken(user.getLogin(), user.getRoles());
        authenticationService.setRefTokenTTL(90L);
        authenticationService.setRefTokenLength(64);
        refreshToken.setValue(refTokenValue);
        expected.put("username", user.getLogin());
        expected.put("jwt", accessToken);
        expected.put("ref" , refreshToken);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(tokenProvider.createToken(anyString(), anySet())).thenReturn(accessToken);
        when(tokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);
        Map<String, Object> actual = authenticationService.registerUser(registerDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void registerUserTrueTwo() {
        Map<String, Object> expected = new HashMap<>();
        registerDTO.setRememberMe(false);
        String accessToken = tokenProvider.createToken(user.getLogin(), user.getRoles());
        expected.put("username", user.getLogin());
        expected.put("jwt", accessToken);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(tokenProvider.createToken(anyString(), anySet())).thenReturn(accessToken);
        Map<String, Object> actual = authenticationService.registerUser(registerDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void refreshTokenTrue() {
        Map<String, Object> expected = new HashMap<>();
        authenticationService.setRefTokenTTL(90L);
        authenticationService.setRefTokenLength(64);
        refreshToken.setCreatedAt(LocalDateTime.now());
        String accessToken = "Hello world";
        expected.put("username", user.getLogin());
        expected.put("jwt", accessToken);
        expected.put("ref" , refreshToken);
        when(tokenRepository.findByValue(anyString())).thenReturn(optionalRefreshToken);
        when(userRepository.findByLogin(anyString())).thenReturn(user);
        when(tokenProvider.createToken(anyString(), anySet())).thenReturn(accessToken);
        when(tokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);
        Map<String, Object> actual = authenticationService.refreshToken(refreshDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void refreshTokenFalseOne() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("error","401");
        LocalDateTime localDateTime = LocalDateTime.of(2020,04,11,14,11,1);
        authenticationService.setRefTokenTTL(90L);
        authenticationService.setRefTokenLength(64);
        refreshToken.setCreatedAt(localDateTime);
        optionalRefreshToken = Optional.of(refreshToken);
        when(tokenRepository.findByValue(anyString())).thenReturn(optionalRefreshToken);
        Map<String, Object> actual = authenticationService.refreshToken(refreshDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void refreshTokenFalseTwo() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("error","401");
        when(tokenRepository.findByValue(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(anyString())).thenReturn(user);
        when(tokenRepository.deleteByUsersId(anyInt())).thenReturn(0);
        Map<String, Object> actual = authenticationService.refreshToken(refreshDTO);
        assertEquals(expected, actual);
    }
}
