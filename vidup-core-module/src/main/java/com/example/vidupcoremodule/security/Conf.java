package com.example.vidupcoremodule.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Configuration
public class Conf {

    @Autowired
    DataSource dataSource;

    /*
    @Bean
    public UserDetailsService jdbcUserDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Customize your queries if needed
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username,password,'true' as enabled from user where username = ?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, r.role_name FROM user u LEFT JOIN user_role ur ON u.id = ur.user_id RIGHT JOIN role r ON ur.role_id=r.id  WHERE u.username = ?");
      /* select username,role_name from
        user as u left join user_role as ur
        on u.id=ur.user_id right join role as r
        on ur.role_id=r.id
        where username=?



        return jdbcUserDetailsManager;
    }
    */
}
