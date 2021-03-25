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

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtTokenFilter jwtTokenFilter;
    private static final String LOGIN_ENDPOINT = "${spring.data.rest.base-path}auth/**";
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
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(NEW_ENDPOINT).hasAuthority("DO_STUFF")
                .anyRequest().permitAll()
                .and()
                .apply(new JwtConfigurer(tokenProvider));
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
