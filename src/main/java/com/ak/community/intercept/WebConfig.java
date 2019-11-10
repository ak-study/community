package com.ak.community.intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private SessionIntercept sessionIntercept;
    @Autowired
    private ProfileIntercept profileIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionIntercept).addPathPatterns("/**");
        //registry.addInterceptor(profileIntercept).addPathPatterns("/profile/**");
    }
}
