package com.example.vidupcoremodule.security;


import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {


    public final String TOKEN_PREFIX = "Bearer ";
    public final String JWT = "JWT";
    private final JwtParser jwtParser;

    private final JwtParser internalApisJwtParser;
    String secretKey = secret();
    String secretKeyInternalApis = secretInternal();
    //TODO change secret and validity
    //  private final String secret_key = "mysecretkey";
    private long accessTokenValidity = 60 * 60;
    private long internalAccessTokenValidity = 60 * 60 * 24 * 7;

    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
        this.internalApisJwtParser = Jwts.parser().setSigningKey(secretKeyInternalApis);
    }

    @Bean
    private String secret() {
        String secret_key = "mysecretkey";
        Random random = new Random();
        return secret_key + "-" + random.nextInt(1000);
    }

    @Bean
    private String secretInternal() {
        String secret_key = "supersecretinternalkey";
        return secret_key;
    }

    public String createToken(UserDetails userDetails) {
        //   Claims claims = Jwts.claims().setSubject(user.getEmail());
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Boolean masterAccess(String jwtToken) {
        try {
            Claims claims = internalApisJwtParser.parseClaimsJws(jwtToken).getBody();
            return claims.getSubject().equals("MASTER");
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }


    public Claims resolveClaims(HttpServletRequest req, String token) {
        try {

            if (token != null) {

                Claims claims = null;
                claims = jwtParser.parseClaimsJws(token).getBody();
                return claims;

            }
            return null;
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (SignatureException ex) {
            ex.printStackTrace();
            req.setAttribute("invalid", ex.getMessage());
            return null;
        }


    }


    public String resolveToken(Cookie[] cookies) {

        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(JWT)) {
                return cookie.getValue();
            }
        }
        return null;
    }


    public boolean validJwtToken(String token) {

        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }


}