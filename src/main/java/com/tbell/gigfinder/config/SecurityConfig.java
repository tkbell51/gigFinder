package com.tbell.gigfinder.config;


import com.tbell.gigfinder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;
import java.util.Collection;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    private String usersQuery = "select username, user_password, active from user_data where username=?";
    private String rolesQuery = "select u.username, r.name from user_data u inner join role r on(u.role_id=r.id) where u.username=?";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/css/styles.css");
        web.ignoring().antMatchers("/assets/js/main.js");
        web.ignoring().antMatchers("/assets/images/**");


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/find-gigs/**").authenticated()
                .antMatchers("/find-bands/**").authenticated()
                .antMatchers("/gig/**").authenticated()
                .antMatchers("/search/**").authenticated()
                .antMatchers("/company/**").hasRole("COMPANY")
                .antMatchers("/musician/**").hasRole("MUSICIAN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/");

    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
            return (request, response, authentication) -> {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority grantedAuthority : authorities) {
                    if (grantedAuthority.getAuthority().equals("ROLE_COMPANY")) {
                        response.sendRedirect("/company/my-profile");
                        return;
                    } else if (grantedAuthority.getAuthority().equals("ROLE_MUSICIAN")) {
                        response.sendRedirect("/musician/my-profile");
                        return;
                    } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                        response.sendRedirect("/admin");
                        return;
                }
            }
    };

    }

    private AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            request.getSession().setAttribute("error", "Cannot login with provided credentials");
            response.sendRedirect("/login");
        };
    }
}

