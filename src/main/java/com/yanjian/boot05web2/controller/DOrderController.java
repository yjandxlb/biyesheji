package com.yanjian.boot05web2.controller;

import cn.hutool.socket.protocol.MsgDecoder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanjian.boot05web2.bean.DOrder;
import com.yanjian.boot05web2.bean.Food;
import com.yanjian.boot05web2.bean.User;
import com.yanjian.boot05web2.service.DOrderService;
import com.yanjian.boot05web2.service.FoodService;
import com.yanjian.boot05web2.service.OrderService;
import com.yanjian.boot05web2.service.impl.DOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class DOrderController {
    @Autowired
  private DOrderService desOrderService=new DOrderServiceImpl();
    @Autowired
    FoodService foodService;
    @Autowired
    OrderService  orderService;

   /* *//**
     * 主菜添加进购物车
     * @param
     * @return
     *//*
   *//* @PostMapping("/zhucai1/gouwuche")
    public String gouwuche(DOrder dOrder,RedirectAttributes ra){
        ra.addAttribute("s1","1");
      if(dOrder.getFnumber()!=0)
        desOrderService.savea1(dOrder);

        return "redirect:/zhucai1";
    }*/

    @GetMapping("/gouwuche")
    public String gouwuche3(Model model, HttpSession httpSession
                            ){
        User user = (User) httpSession.getAttribute("loginUser");
        List<DOrder> ufoods = desOrderService.list(new QueryWrapper<DOrder>().eq("uid", user.getUid()));
        List<Food>  foods=new ArrayList<>();
        for (DOrder ufood : ufoods) {
            Food fid = foodService.getOne(new QueryWrapper<Food>().eq("fid", ufood.getFid()));
            foods.add(fid);
            System.out.println(fid);
        }
//        httpSession.setAttribute("f21","123");
        model.addAttribute("ufoods",ufoods);
        model.addAttribute("foods",foods);
        return "gouwuche";
    }
    //添加购物车
    @PostMapping("/gouwuche")
    public String gouwuche4(DOrder dOrder,Model model){
        if(dOrder.getFnumber()!=0)
        {  desOrderService.savea1(dOrder);
        model.addAttribute("dOrder",dOrder);
        Food fid = foodService.getOne(new QueryWrapper<Food>().eq("fid", dOrder.getFid()));
        model.addAttribute("food",fid);
        return "wcgouwuche";}
        else {
            return "redirect:/index.html";
        }
    }
    @GetMapping("/gouwuche/delete/{fid}")
    public String delete(@PathVariable("fid") int fid,@RequestParam("uid")int uid){

        desOrderService.remove(new QueryWrapper<DOrder>().eq("fid",fid).eq("uid",uid));

        return "redirect:/gouwuche";
    }

    @GetMapping("/gouwuche/add/{fid}")
    public String add(@PathVariable("fid") int fid,@RequestParam("uid")int uid){
     desOrderService.add1(fid,uid);

        return "redirect:/gouwuche";
    }
    @GetMapping("/gouwuche/minus/{fid}")
    public String minus(@PathVariable("fid") int fid,@RequestParam("uid")int uid){
        desOrderService.minus1(fid,uid);
        return "redirect:/gouwuche";
    }

    @PostMapping("/sfoods/gouwuche")
    public String sfoods(DOrder dOrder,RedirectAttributes ra){
        if(dOrder.getFnumber()!=0)
            desOrderService.savea1(dOrder);
   ra.addAttribute("msg","添加购物车成功");
        return "redirect:/searchfoods.html";
    }

}
