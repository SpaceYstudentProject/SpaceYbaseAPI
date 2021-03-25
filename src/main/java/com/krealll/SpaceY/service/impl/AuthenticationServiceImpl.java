package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.RefreshToken;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.repository.TokenRepository;
import com.krealll.SpaceY.security.TokenProvider;
import com.krealll.SpaceY.service.AuthenticationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${spacey.ref.len}")
    private Integer refTokenLength;

    @Value("${spacey.ref.ttl}")
    private Long refTokenTTL;

    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthenticationServiceImpl(TokenRepository tokenRepository, TokenProvider tokenProvider) {
        this.tokenRepository = tokenRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Map<String, Object> resolveToken(User user, boolean rememberMe) {
        Map<String, Object> response = new HashMap<>();
        if(rememberMe){
            RefreshToken refreshToken = new RefreshToken();
            String accessToken = tokenProvider.createToken(user.getLogin(), user.getRoles());
            String refTokenValue = RandomStringUtils.randomAlphanumeric(refTokenLength);
            refreshToken.setValue(refTokenValue);
            refreshToken.setExpiresIn(refTokenTTL);
            refreshToken.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            refreshToken.setUsersId(user.getId());
            RefreshToken refToken = tokenRepository.save(refreshToken);
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
}
