package com.example.vidupcoremodule.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {

    @Autowired
    @Qualifier("LoginHandlerImpl")
    AuthenticationSuccessHandler successHandler;

    @Autowired
    DataSource dataSource;



    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutHandler logoutHandler(){ return new LogoutHandlerImpl();}

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){return successHandler;}

    @Bean
    public JwtFilter jwtFilter(){return new JwtFilter();}

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint()
    {
        return http401And403Handler();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler()
    {
        return http401And403Handler();
    }
    @Bean
    public Http401And403Handler http401And403Handler() {return new Http401And403Handler();}



    @Bean
    public UserDetailsService jdbcUserDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Customize your queries if needed
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username,password,'true' as enabled from user where username = ?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, r.role_name FROM user u LEFT JOIN user_role ur ON u.id = ur.user_id RIGHT JOIN role r ON ur.role_id=r.id  WHERE u.username = ?");


        return jdbcUserDetailsManager;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      //  authProvider.setUserDetailsService(userDetailsService());
        authProvider.setUserDetailsService(jdbcUserDetailsManager());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    WebSecurityCustomizer configureWebSecurity() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/css/**", "/webjars/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {

        http.authorizeHttpRequests
                                (
                                    authorize -> authorize

                                            //resources


                                            .requestMatchers("/auth/**", "/dist/**").permitAll()
                                            .requestMatchers("/css/**", "/js/**").permitAll()
                                            .requestMatchers("/auth/**", "/dist/**").permitAll()
                                            .requestMatchers("/users/{userId}/profile_img").permitAll()
                                            .requestMatchers("/users/{userName}/profile_img_by_uname").permitAll()

                                            //permitted endpoints
                                            .requestMatchers("/").permitAll()
                                            .requestMatchers("/login","/401","/403").permitAll()
                                            .requestMatchers("/users/register/**").permitAll()
                                            .requestMatchers("/videos/permitted/**").permitAll()
                                            .requestMatchers("/users/permitted/**").permitAll()




                                            //admin endpoints
                                            .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")

                                            //common endpoints
                                            .requestMatchers("/videos/processing/**").hasAuthority("MASTER")

                                            .requestMatchers("/live_record/**").hasAnyAuthority("ADMIN","USER")
                                            .requestMatchers("/statistics/**").hasAnyAuthority("ADMIN","USER")
                                            .requestMatchers("/playlists/**").hasAnyAuthority("ADMIN","USER")
                                            .requestMatchers("/users/**").hasAnyAuthority("ADMIN","USER")
                                            .requestMatchers("/videos/**").hasAnyAuthority("ADMIN","USER")
                                            .requestMatchers("/logout").hasAnyAuthority("ADMIN","USER")
                                            .requestMatchers("/subscriptions/**").hasAnyAuthority("ADMIN","USER")


                                      //      .requestMatchers("/resources/static/**").permitAll()
                                        //    .requestMatchers("/resources/templates/**").permitAll()
                                            .anyRequest().permitAll()


                                )

                .formLogin(form ->  form.permitAll().loginPage("/login").defaultSuccessUrl("/").successHandler(successHandler))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .addLogoutHandler(logoutHandler())
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .deleteCookies("JWT")
                        .invalidateHttpSession(true))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex-> ex.authenticationEntryPoint(authenticationEntryPoint()).accessDeniedHandler(accessDeniedHandler()));
        return http.build();


    }





}
