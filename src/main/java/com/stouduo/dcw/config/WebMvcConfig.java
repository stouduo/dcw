package com.stouduo.dcw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addRedirectViewController("/","/form/desktop");
        registry.addViewController("/formData").setViewName("formData");
        registry.addViewController("/tosignup").setViewName("signup");
        registry.addViewController("/register").setViewName("register");
    }
}