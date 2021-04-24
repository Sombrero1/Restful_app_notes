package com.example.trpp_project.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.trpp_project.models.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private long duration = 86_400_000; //1 день
    private String secret = "test";
    private String HEADER_STRING = "Authorization";
    private String TOKEN_PREFIX = "Bearer ";

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            User credUser = new ObjectMapper().readValue(request.getInputStream(),User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credUser.getUsername(),
                            credUser.getPassword(),
                            Collections.emptyList()

                    )
            );

        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException();

        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(Algorithm.HMAC256(secret.getBytes()));
        response.addHeader(HEADER_STRING,TOKEN_PREFIX + token);
    }
}
