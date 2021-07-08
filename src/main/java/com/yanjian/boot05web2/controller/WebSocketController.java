package com.yanjian.boot05web2.controller;

import com.tencentcloudapi.cdn.v20180606.models.Https;
import com.yanjian.boot05web2.bean.User;
import com.yanjian.boot05web2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    UserService userService;
    @MessageMapping("/test")
    @SendTo("/topic/test")
    @ResponseBody
    public String call(String msg) throws InterruptedException {
       /* Thread.sleep(1000); // simulated delay*/
        System.out.println("fasongmsg");
        System.out.println(msg);
        return msg;
    }

    @MessageMapping("/test/{name}")
    @SendTo("/topic/main")
    @ResponseBody
    public String send1(String msg, @DestinationVariable("name")String username) throws InterruptedException {
        System.out.println(username);

       /* Thread.sleep(1000); // simulated delay*/
        System.out.println("fasongmsgtestname");
        simpMessagingTemplate.convertAndSend("/topic/test/"+username,"用户："+msg);

        return "用户："+msg;
    }
    @MessageMapping("/test1/{name}")
    @SendTo("/topic/main")
    @ResponseBody
    public String send11(String msg, @DestinationVariable("name")String username) throws InterruptedException {
        System.out.println(username);
/*
        Thread.sleep(1000); // simulated delay*/
        System.out.println("fasongmsgtestname");
        simpMessagingTemplate.convertAndSend("/topic/test/"+username,"商家："+msg);

        return "商家："+msg;
    }
    @MessageMapping("/main")
    @SendTo("/topic/main")
    @ResponseBody
    public String main1(String msg) throws InterruptedException {
        System.out.println("main");


        System.out.println(msg);
        return msg;
    }

    @RequestMapping("/test01")
    @ResponseBody
    public String send() {
        System.out.println("发送成功");
        simpMessagingTemplate.convertAndSend("/topic/test", "send.....");

        return "调用成功！";
    }

    @RequestMapping("/socketPage")
    public String socketPage(){
        System.out.println("socketpage");
        return "liaotian";
    }

    @SubscribeMapping("/topic/test")
    @ResponseBody
    public String test02(){
        System.out.println("topic/test");
        String msg = " @SubscribeMapping";
        System.out.println(msg);
        return msg;
    }
    @GetMapping("/chat")
    public String chat(@RequestParam("username")String username, Model model){

        model.addAttribute("username",username);
        return "chat";
    }

}
