package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.RefreshToken;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.RefreshDTO;
import com.krealll.SpaceY.model.dto.RegisterDTO;
import com.krealll.SpaceY.repository.TokenRepository;
import com.krealll.SpaceY.repository.UserRepository;
import com.krealll.SpaceY.security.TokenProvider;
import com.krealll.SpaceY.service.AuthenticationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${spacey.ref.len}")
    private Integer refTokenLength;

    @Value("${spacey.ref.ttl}")
    private Long refTokenTTL;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthenticationServiceImpl(BCryptPasswordEncoder encoder, UserRepository userRepository, TokenRepository tokenRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Map<String, Object> resolveToken(User user, boolean rememberMe) {
        Map<String, Object> response = new HashMap<>();
        if(rememberMe){
            RefreshToken refreshToken = new RefreshToken();
            String accessToken = tokenProvider.createToken(user.getLogin(), user.getRoles());
            RefreshToken refToken = createRefresh(user);
            response.put("username", user.getLogin());
            response.put("jwt", accessToken);
            response.put("ref" , refToken);
            return response;
        } else {
            String accessToken = tokenProvider.createToken(user.getLogin(), user.getRoles());
            response.put("username", user.getLogin());
            response.put("jwt", accessToken);
            return response;
        }
    }

    @Override
    public boolean logoutAll(Integer userId) {
        boolean result;
        int num =  tokenRepository.deleteByUsersId(userId);
        result = num > 0;
        return result;
    }

    @Override
    public boolean logout(String token) {
        boolean result;
        int num =  tokenRepository.deleteByValue(token);
        result = num > 0;
        return result;
    }

    @Override
    public Map<String, Object> registerUser(RegisterDTO registerDTO) {
        User createdUser = userRepository.save(registerDTO.toUser());
        Map<String, Object> response = new HashMap<>();
        if(registerDTO.isRememberMe()){
            String accessToken = tokenProvider.createToken(createdUser.getLogin(), createdUser.getRoles());
            RefreshToken refToken = createRefresh(createdUser);
            response.put("username", createdUser.getLogin());
            response.put("jwt", accessToken);
            response.put("ref" , refToken);
            return response;
        } else {
            String accessToken = tokenProvider.createToken(createdUser.getLogin(), createdUser.getRoles());
            response.put("username", createdUser.getLogin());
            response.put("jwt", accessToken);
            return response;
        }
    }

    @Override
    public Map<String, Object> refreshToken(RefreshDTO refreshDTO) {
        Map<String, Object> response = new HashMap<>();
        Optional<RefreshToken> tokenOptional = tokenRepository.findByValue(refreshDTO.getToken());
        if(tokenOptional.isPresent()){
            Long expiresIn = tokenOptional.get().getExpiresIn();
            LocalDateTime created = tokenOptional.get().getCreatedAt();
            LocalDateTime expCheck = created.plusSeconds(expiresIn);
            boolean expired = expCheck.isBefore(LocalDateTime.now());
            if(expired){
                response.put("error", "401");
                return response;
            } else {
                User user = userRepository.findByLogin(refreshDTO.getUsername());
                String accessToken = tokenProvider.createToken(user.getLogin(), user.getRoles());
                tokenRepository.deleteByValue(tokenOptional.get().getValue());
                RefreshToken refToken = createRefresh(user);
                response.put("username", user.getLogin());
                response.put("jwt", accessToken);
                response.put("ref" , refToken);
                return response;
            }

        } else {
            User user  = userRepository.findByLogin(refreshDTO.getUsername());
            tokenRepository.deleteByUsersId(user.getId());
            response.put("error","401");
            return response;
        }
    }


    private RefreshToken createRefresh(User user){
        RefreshToken refreshToken = new RefreshToken();
        String refTokenValue = RandomStringUtils.randomAlphanumeric(refTokenLength);
        refreshToken.setValue(refTokenValue);
        refreshToken.setExpiresIn(refTokenTTL);
        refreshToken.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        refreshToken.setUsersId(user.getId());
        return  tokenRepository.save(refreshToken);
    }
}
