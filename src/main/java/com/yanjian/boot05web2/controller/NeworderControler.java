package com.yanjian.boot05web2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yanjian.boot05web2.bean.Neworder;
import com.yanjian.boot05web2.bean.User;
import com.yanjian.boot05web2.service.NeworderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class NeworderControler {
    @Autowired
    NeworderService neworderService;
    @GetMapping("/neworder")
    public String neworder(Model model,@RequestParam(value = "username",required = false)String username){
        if(username==null||"".equals(username))
        {
            List<Neworder> list = neworderService.list();
            model.addAttribute("neworder",list);
        }
        else {
            List<Neworder> username1 = neworderService.list(new QueryWrapper<Neworder>().eq("username", username));
            model.addAttribute("neworder",username1);
        }
        return "neworder";
    }

    @GetMapping("/neworder/acc")
    public String acc(@RequestParam("oid")Long oid,HttpSession httpSession){
        System.out.println(oid);
        int acc = (int) httpSession.getAttribute("acc");
        Neworder oid1 = neworderService.getOne(new QueryWrapper<Neworder>().eq("oid", oid));
        if(oid1.getAcc().equals("0")) {
            httpSession.setAttribute("acc",--acc);
            neworderService.update(new UpdateWrapper<Neworder>().eq("oid", oid).
                    set("acc", 1));
        }
        return "redirect:/neworder";

    }

    @GetMapping("/neworder/suc")
    public String suc(@RequestParam("oid")Long oid, HttpSession httpSession/* RedirectAttributes ra*/){
        System.out.println(oid);
        User loginUser = (User) httpSession.getAttribute("loginUser");
       /* ra.addAttribute("username",loginUser.getUsername());*/
        neworderService.remove(new QueryWrapper<Neworder>().eq("oid",oid));
        return "redirect:/neworder";
    }

}
