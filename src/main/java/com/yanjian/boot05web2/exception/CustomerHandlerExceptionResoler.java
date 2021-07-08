package com.yanjian.boot05web2.exception;

import org.apache.coyote.Response;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Order(value = Ordered.HIGHEST_PRECEDENCE)  //数字越小优先级越高
@Component
public class CustomerHandlerExceptionResoler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse, Object o, Exception e) {
        try {
            httpServletResponse.sendError(511,"我喜欢的错误");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new ModelAndView();
    }
}
