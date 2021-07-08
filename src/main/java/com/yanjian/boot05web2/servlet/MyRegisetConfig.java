package com.yanjian.boot05web2.servlet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import java.util.Arrays;

@Configuration
public class MyRegisetConfig {
    @Bean
    public ServletRegistrationBean myServlet(){
       Myservlet myservlet=new Myservlet();
        return  new ServletRegistrationBean(myservlet,"/my","/my01");
    }
    @Bean
    public FilterRegistrationBean myFilter(){
        MyFilter myFilter=new MyFilter();
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean(myFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/my"));
        return filterRegistrationBean;
    }
    @Bean
    public ServletListenerRegistrationBean myListener(){
    MyServletContextListener contextListener=new MyServletContextListener();
    return new ServletListenerRegistrationBean(contextListener);
    }
}
