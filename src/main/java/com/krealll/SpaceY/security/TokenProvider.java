package com.krealll.SpaceY.security;

import com.krealll.SpaceY.model.Role;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Component
public class TokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${spacey.ref.len}")
    private String refTokenLength;

    @Value("${spacey.jwt.ttl}")
    private Long timeToLive;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public  String createToken(String login , Set<Role> roles){
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("authorities", TokenInfoFactory.mapToGrantedAuthorities(roles));
        Date current = new Date();
        Date expirationTime = new Date(current.getTime() + timeToLive);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(current)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }


    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getLogin(token));
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }

    public String getLogin(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getAuthorities(String token){
        List<String> roles = (List<String>)Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("roles");
        if(roles == null){
            log.error("ERRRRRRRR");
        }
        return roles;
    }


    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken (String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
            return true;
        } catch (ExpiredJwtException e){
            log.warn("Token -" + token + " was expired");
        } catch (UnsupportedJwtException e){
            log.warn("Unsupported jwt exception");
        } catch (MalformedJwtException e){
            log.warn("Token is malformed");
        } catch (SignatureException e){
            log.warn("Invalid token signature");
        } catch (Exception e){
            log.warn("Invalid token");
        }
        return false;
    }

    private List<String> getRoleNames( List<Role> roles){
        List<String> result  = new ArrayList<>();
        roles.forEach(role -> {
            result.add(role.getName());
        });
        return result;
    }
}
