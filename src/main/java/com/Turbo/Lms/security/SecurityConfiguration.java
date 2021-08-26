package com.Turbo.Lms.security;

import com.Turbo.Lms.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    public void authConfigure(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder,
                              UserAuthService userDetailService) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("student")
                .password(passwordEncoder.encode("123"))
                .roles("STUDENT");
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN");
        auth.userDetailsService(userDetailService);
    }

    @Configuration
    public static class UiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.
                    authorizeRequests()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/", "/default.css", "/registration", "/login/**").permitAll()
                        .anyRequest().authenticated()
                    .and()
                        .formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/course")
                        .permitAll()
                    .and()
                        .logout()
                        .permitAll()
                    .and()
                        .exceptionHandling()
                        .accessDeniedPage("/access_denied");
        }
    }
}
