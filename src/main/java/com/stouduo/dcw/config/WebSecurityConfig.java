package com.stouduo.dcw.config;

import com.stouduo.dcw.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {
                return null;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(MD5Util.encode((String) rawPassword));
            }
        }); //user Details Service验证;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
//                .antMatchers("/**").permitAll()
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("tosignup","/register","/user/captcha","/form/view/**", "/form/result/**", "/user/signup/**", "/user/active").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").successForwardUrl("/user/loginSuccess").failureUrl("/login?error").permitAll()
//                .and().rememberMe().rememberMeParameter("rememberMe").tokenValiditySeconds(60 * 60 * 24 * 7).rememberMeCookieName("dcw")
                .and().logout().permitAll();

    }
}