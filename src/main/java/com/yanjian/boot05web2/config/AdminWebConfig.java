package com.yanjian.boot05web2.config;

import com.yanjian.boot05web2.interceptor.LoginInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 1、编写一个拦截器实现handlerInterceptor接口
 * 2、拦截器注册到容器中（实现webMvcConfigurer的addinterceptors方法）
 * 3、指定拦截规则（如果是全部拦截，静态文件也会被拦截)
 *
 * @EnableWebMvc：全面接管
 *      静态资源？视图解析器。。。全部失效
 */
//@EnableWebMvc
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    /**
     * 定义静态资源行为
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        访问/aa 下面的请求都去类路径下的static找。
        registry.addResourceHandler("/aa/**").addResourceLocations("classpath:/static/");
    }

  @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/","/login",
                "/css/**","/fonts/**","/images/**","/js/**","/aa/**","/registration","/registration1","/re","/error",
                "/test","/notify","/test1","/yz","/yzm","/forget","/forget.html","/reset","/getone");
    }

//    @Bean
   /* public WebMvcRegistrations webMvcRegistrations(){
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return null;
            }
        };
    }*/
}
