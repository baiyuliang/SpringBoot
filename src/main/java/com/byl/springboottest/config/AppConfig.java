package com.byl.springboottest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //浏览器请求映射到对应页面（ViewName代表对应的html）
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/reg").setViewName("reg");
        registry.addViewController("user/userlist").setViewName("user/userlist");
        registry.addViewController("user/add").setViewName("user/add");
        registry.addViewController("user/edit").setViewName("user/edit");
        registry.addViewController("/level1").setViewName("level1/index");
        registry.addViewController("/level2").setViewName("level2/index");
        registry.addViewController("/level3").setViewName("level3/index");
    }

}