package com.example.vidupcoremodule.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * SavedRequestAwareAuthenticationSuccessHandler makes use of the saved request stored in the session.
 * After a successful login, users will be redirected to the URL saved in the original request.
 * <p>
 * Upon successful authentication , store session details in session table , handle pending notifications ,
 * call super.onAuthenticationSuccess() to continue to destination  url
 */
@Qualifier("LoginHandlerImpl")
@Component
public class LoginHandlerImpl implements AuthenticationSuccessHandler {


    @Autowired
    JwtUtil jwtUtil;


    /**
     * * Upon successful authentication , call execLoginSuccessOperations()
     * * Upon Authentication success redirect to "/"
     *
     * @param request        Incoming HTTP Request
     * @param response       HTTP Response
     * @param authentication Authenticated User
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        // String jwtToken = jwtUtil.createToken(utilClass.getUserDetailsFromAuthentication(authentication).getUser());
        String jwtToken = jwtUtil.createToken((UserDetails) authentication.getPrincipal());
        execLoginOperations(request, response, authentication, jwtToken);
        response.sendRedirect(request.getContextPath() + "/");

    }


    private void execLoginOperations(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String jwtToken) {

        //  storeSessionDetailsInDatabase(request, authentication, jwtToken);


        //adding JWT Token in cookie and authorization header
        response.addHeader("Authorization", jwtToken);
        response.addHeader(jwtUtil.JWT, jwtToken);
        Cookie cookie = new Cookie("JWT", jwtToken);
        response.addCookie(cookie);
    }


}
