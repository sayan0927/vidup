package com.example.vidupcoremodule.security;

import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    Conf conf;

    @Autowired
    UserDetailsService jdbcUserDetailsManager;


    UserDetailsService userService;

    @PostConstruct
    void init() {
        userService = jdbcUserDetailsManager;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String jwtToken = jwtUtil.resolveToken(request.getCookies());

        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }


        if (jwtUtil.masterAccess(jwtToken)) {

            Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("MASTER"));

            Authentication authentication = new PreAuthenticatedAuthenticationToken("MASTER", jwtToken, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (jwtUtil.validJwtToken(jwtToken)) {

            Claims claims = jwtUtil.resolveClaims(request, jwtToken);

            String username = claims.getSubject();

            UserDetails userDetails = userService.loadUserByUsername(username);

            //  userDetails.setSessionToken(jwtToken);
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, jwtToken, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }


        filterChain.doFilter(request, response);


    }
}
