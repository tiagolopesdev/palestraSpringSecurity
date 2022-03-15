/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.palestraSpringSecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author tiagolopes
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/projetos").hasAnyRole("PG_PROJETOS")
                .antMatchers("/relatorio-equipe").hasAnyRole("PG_REL_EQUIPE")
                .antMatchers("/relatorio-custos").hasAnyRole("PG_REL_CUSTOS")
                .anyRequest()
                .authenticated()
            .and().formLogin()
                .loginPage("/entrar").permitAll();
    }

}
