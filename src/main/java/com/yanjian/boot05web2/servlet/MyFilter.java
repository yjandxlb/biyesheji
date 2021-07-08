package com.yanjian.boot05web2.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
@Slf4j
//@WebFilter(urlPatterns = {"/css/*","/images/*"})
public class MyFilter  implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    log.info("filter开始");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    log.info("filter工作");
    filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        log.info("filter 结束");
    }
}
