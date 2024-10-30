package com.example.vidupcoremodule.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {


    private void customTest() {


    }

    /**
     * Root mapping redirects to the all videos page.
     *
     * @return Forward to the all videos page
     */
    @GetMapping("/")
    String rootMapping() {


        customTest();
        return "forward:/videos/permitted/all/page";
    }


    /**
     * Handles unauthorized access (HTTP 401).
     *
     * @return ModelAndView for the login page with an unauthorized status
     */
    @GetMapping("/401")
    public ModelAndView unauthorized() {
        ModelAndView m = new ModelAndView("login");
        m.addObject("msg", "Please Login");
        m.setStatus(HttpStatus.UNAUTHORIZED);
        return m;
    }

    /**
     * Handles access denied (HTTP 403).
     *
     * @return ModelAndView for the login page with an unauthorized status
     */
    @GetMapping("/403")
    public ModelAndView accessDenied() {
        ModelAndView m = new ModelAndView("login");
        m.addObject("msg", "Please Login with proper privileges");
        m.setStatus(HttpStatus.UNAUTHORIZED);
        return m;
    }

    /**
     * Returns the login page.
     *
     * @return ModelAndView for the login page
     */
    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }


    /**
     * Handles generic errors (HTTP 500).
     *
     * @return ModelAndView for the error page with an internal server error status
     */
    @GetMapping("/error")
    public ModelAndView error() {
        ModelAndView m = new ModelAndView("error");
        m.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return m;
    }


}
