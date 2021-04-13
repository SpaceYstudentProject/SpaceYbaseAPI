package com.krealll.SpaceY.config;

import com.krealll.SpaceY.security.jwt.JwtConfigurer;
import com.krealll.SpaceY.security.jwt.JwtTokenFilter;
import com.krealll.SpaceY.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtTokenFilter jwtTokenFilter;
    private static final String LOGIN_ENDPOINT = "${spring.data.rest.base-path}auth/";
    private static final String LOGOUT = LOGIN_ENDPOINT + "logout**";
    private static final String REFRESH = LOGIN_ENDPOINT + "refresh";
    private static final String REGISTER = LOGIN_ENDPOINT + "register";
    private static final String LOGIN = LOGIN_ENDPOINT + "login";
    private static final String USER_ENDPOINT = "${spring.data.rest.base-path}user/**";
    private static final String NEW_ENDPOINT = "${spring.data.rest.base-path}test/**";

    @Autowired
    public SecurityConfig(TokenProvider tokenProvider, JwtTokenFilter jwtTokenFilter) {
        this.tokenProvider = tokenProvider;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    public SecurityConfig(boolean disableDefaults, TokenProvider tokenProvider, JwtTokenFilter jwtTokenFilter) {
        super(disableDefaults);
        this.tokenProvider = tokenProvider;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGOUT).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(LOGIN).permitAll()
                .antMatchers(REFRESH).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(REGISTER).permitAll()
                .antMatchers(USER_ENDPOINT).permitAll()
                .antMatchers(NEW_ENDPOINT).hasAuthority("DO_STUFF")
                .anyRequest().permitAll()
                .and()
                .apply(new JwtConfigurer(tokenProvider));
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                        .allowedHeaders("*")
                        .allowedOrigins("*");
            }
        };
    }
}
