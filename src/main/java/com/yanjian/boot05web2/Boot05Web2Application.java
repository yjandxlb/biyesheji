package com.yanjian.boot05web2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
@MapperScan("com.yanjian.boot05web2.mapper")
@ServletComponentScan("com.yanjian.boot05web2")
@SpringBootApplication
public class Boot05Web2Application {

    public static void main(String[] args)
    {

        SpringApplication.run(Boot05Web2Application.class, args);
    }

}
