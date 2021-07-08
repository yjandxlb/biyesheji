package com.yanjian.boot05web2.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.yanjian.boot05web2.bean.Food;
import com.yanjian.boot05web2.bean.Neworder;
import com.yanjian.boot05web2.bean.User;
import com.yanjian.boot05web2.config.Captcha;
import com.yanjian.boot05web2.config.SendSms;
import com.yanjian.boot05web2.service.FoodService;
import com.yanjian.boot05web2.service.NeworderService;
import com.yanjian.boot05web2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    UserService userService;
    @Autowired
    FoodService foodService;
    @Autowired
    NeworderService neworderService;
    @GetMapping(value = {"/","/login"})
    public String loginPage(){
        System.out.println("////");
        return "login";
    }
    @PostMapping("/login")
    public String login(User user, HttpSession session, Model model){

        QueryWrapper<User> qw=new QueryWrapper<>();
        User one = userService.getOne(qw.eq("username", user.getUsername()).eq("password", user.getPassword()));
        System.out.println(one);
        if(one!=null){

            session.setAttribute("loginUser",one);
            return "redirect:/index.html";
        }else {
            model.addAttribute("msg","账号或者密码错误");
            return "login";
        }

    }
    @GetMapping("/index.html")
    public String index(Model model,HttpSession httpSession){
      /*  Object loginUser = httpSession.getAttribute("loginUser");
        if(loginUser!=null)
            return "index";
        else {
        model.addAttribute("msg","请先登录");
        return "login";
    }*/
        User loginUser = (User) httpSession.getAttribute("loginUser");

        List<Neworder> list = neworderService.list();
        List<Neworder> acc = neworderService.list(new QueryWrapper<Neworder>().eq("acc", 0));
        int size1 = acc.size();
        int size = list.size();
        if(loginUser.getAdm()==1)
        {
            httpSession.setAttribute("acc",size1);
            httpSession.setAttribute("size",size);
            return "redirect:/zhucai1";
        }
        List<Food> list1 = foodService.list(new QueryWrapper<Food>().orderByDesc("number"));
        List<Food> food1 =new ArrayList<>();
        int i=1;
        for (Food food : list1) {
            if( i<=6) {
                food1.add(food);
                i++;
            }
        }
        List<Food> list2 = foodService.list(new QueryWrapper<Food>().orderByDesc("price"));
        List<Food> food2=new ArrayList<>();
        int j=1;
        for (Food food : list2) {
            if( j<=6) {
                food2.add(food);
                j++;
            }
        }

        model.addAttribute("food1",food1);
        model.addAttribute("food2",food2);

        return "index";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    //验证用户名是否可用,是否存在用户
    @ResponseBody
    @GetMapping("/re")
    public Map<String, Object> registration1(@RequestParam("username")String username) {
        User username1 = userService.getOne(new QueryWrapper<User>().eq("username", username));
        Map<String, Object> map = new HashMap<>();
        if (username1 != null) {
            map.put("userExsit", true);
            map.put("msg", "用户已存在");
        } else {
            map.put("userExsit", false);
            map.put("msg", "用户名可使用");
        }
        System.out.println(map);
        return map;
    }
    //登录滑动验证
    @ResponseBody
    @PostMapping("/yz")
    public String yz(@RequestParam("ticket")String ticket,@RequestParam("randstr")String randStr){
        System.out.println("yz");
        System.out.println(ticket);
        System.out.println(randStr);
        String yanzhengma = Captcha.yanzhengma(ticket, randStr);
        System.out.println(yanzhengma);
        return yanzhengma;
    }
    //注册手机短信验证
    @ResponseBody
    @GetMapping("/yzm")
    public String yzm(@RequestParam("telphone")String telphone){
        System.out.println(telphone);
        String sms = SendSms.sms(telphone);
        return sms;

    }


    //找回密码，输入密码是否正确
    @ResponseBody
    @GetMapping("/forget")
    public Map<String, Object> forget(@RequestParam("username")String username,
                                      @RequestParam("telphone")String telphone){

        boolean forget = userService.forget(username, telphone);
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        if(forget){
            stringStringHashMap.put("userExsit",true);
        }else {
            stringStringHashMap.put("userExsit",false);
        }
        return stringStringHashMap;
    }

    //修改密码，跳到登录界面
    @PostMapping("/reset")
    public String reset(@RequestParam("username")String username,
                        @RequestParam("password")String password){
        System.out.println(username);
        userService.reset(username,password);
        return "login";
    }

    //跳到找回密码界面
    @GetMapping("/forget.html")
    public String forget(){

        return "forget";
    }

    @PostMapping("/registration")
    public String registration1(User user,Model model){
        System.out.println(user);
        if("".equals(user.getPassword())||"".equals(user.getUsername())||"".equals(user.getTelphone())){
            return "registration";
        }
        QueryWrapper<User> qw=new QueryWrapper<>();
        User username = userService.getOne(qw.eq("username", user.getUsername()));
        if(username!=null) {
            model.addAttribute("msg", "用户名已存在");
            return "registration";
        }
        else {
                userService.save(user);
                return "login";
        }
    }

    @GetMapping("/update")
    public String update1(){

        return "update";
    }
    @PostMapping("/update")
    public String update(User user, HttpSession session, Model model,
                         @RequestParam(name = "password1") String password1){

        boolean b = userService.update1(user,password1);

        if(b){
            User one = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
            session.setAttribute("loginUser",one);
            return "redirect:/index.html";
        }
        model.addAttribute("msg","旧密码不正确");
        return "update";
    }
    @GetMapping("/exit")
    public String exit(HttpSession httpSession){
        httpSession.removeAttribute("loginUser");
        return "login";
    }
}
