package com.igitras.configuration;


import static org.springframework.http.HttpMethod.OPTIONS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * Created by mason on 4/19/16.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private FilterSecurityInterceptor filterSecurityInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(filterSecurityInterceptor);

        http.headers().frameOptions().disable()
                .and().authorizeRequests()
                .antMatchers(OPTIONS, "/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/logout", "/api/**").authenticated()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
