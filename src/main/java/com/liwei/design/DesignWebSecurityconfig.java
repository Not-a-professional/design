package com.liwei.design;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class DesignWebSecurityconfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasAuthority("ROLE_ADMIN")
                .antMatchers("/user/**", "/home", "/file/**", "/share/**")
                .hasAnyRole("USER","ADMIN")
                .and().formLogin().loginPage("/login")
                .failureUrl("/login?error=true")
                .and().logout().logoutUrl("/logout").invalidateHttpSession(true)
                .logoutSuccessUrl("/login").permitAll();

        http.sessionManagement().maximumSessions(1).expiredUrl("/login");

        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("static/**");
    }
}
