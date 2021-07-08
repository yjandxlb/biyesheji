package com.yanjian.boot05web2.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
@Slf4j
//@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    log.info("listener 开始工作");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
