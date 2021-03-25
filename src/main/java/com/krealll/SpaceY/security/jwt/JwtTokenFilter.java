package com.krealll.SpaceY.security.jwt;

import com.krealll.SpaceY.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class JwtTokenFilter extends GenericFilter {

    private final TokenProvider tokenProvider;

    @Autowired
    public JwtTokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        String token = tokenProvider.resolveToken((HttpServletRequest) req);

        if (token != null && tokenProvider.validateToken(token)) {
            Authentication auth = tokenProvider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(req, res);
    }
}
