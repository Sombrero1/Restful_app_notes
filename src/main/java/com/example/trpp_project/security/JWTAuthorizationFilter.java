package com.example.trpp_project.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.trpp_project.config.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private String SECRET = Const.SECRET;
    private String HEADER_STRING = Const.HEADER_STRING;
    private String TOKEN_PREFIX = Const.TOKEN_PREFIX;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        log.info("query with header : {}",header);

        if (header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING);
        if (token != null){
            String user;
            try{
                user = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                        .build()
                        .verify(token.replace(TOKEN_PREFIX,""))
                        .getSubject();
            }
            catch (JWTDecodeException | TokenExpiredException e){
                return null;
            }
            if (user != null)
                return new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());
        }
        return null;


    }
}
