package com.yanjian.boot05web2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yanjian.boot05web2.bean.DesOrder;
import com.yanjian.boot05web2.bean.Food;
import com.yanjian.boot05web2.bean.Nopay;
import com.yanjian.boot05web2.bean.Order;
import com.yanjian.boot05web2.service.DesOrderService;
import com.yanjian.boot05web2.service.FoodService;
import com.yanjian.boot05web2.service.NopayService;
import com.yanjian.boot05web2.service.OrderService;
import org.ietf.jgss.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.print.attribute.standard.Fidelity;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FoodController {
    @Autowired
    OrderService orderService;
    @Autowired
    NopayService nopayService;
    @Autowired
    FoodService foodService;
    private String pxiaochi = "src/main/resources/static/images/xiaochi";
    private String pzhucai1 = "src/main/resources/static/images/zhucai1";
   /* @Autowired
    DesOrderService desOrderService;*/
    @GetMapping("/zhucai1")
    public String findAll(Model model){
        List<Food> foods = foodService.list(new QueryWrapper<Food>().eq("vid", 1));
        model.addAttribute("foods",foods);
        return "zhucai1";
    }
   /* @PostMapping("/zhucai1/gouwuche")
    public String gouwuche(DesOrder desOrder){
        System.out.println(desOrder);
        desOrderService.save(desOrder);
        return "redirect:/zhucai1";
    }*/
    @GetMapping("/xiaochi")
    public String xiaochiAll(Model model ){
        List<Food> vid = foodService.list(new QueryWrapper<Food>().eq("vid", 2));
        model.addAttribute("xiaochi",vid);
        return "xiaochi";
    }

    @PostMapping("/search/food")
    public String search(@RequestParam("keyword")String keyword, RedirectAttributes ra){
        ra.addAttribute("keyword",keyword);
        return "redirect:/searchfoods.html";
    }
    @GetMapping("/searchfoods.html")
    public String serachfoods(Model model, @RequestParam(value = "keyword",required = false)String keyword,
                              @RequestParam(value = "msg",required = false)String msg){
        List<Food> foods = foodService.list(new QueryWrapper<Food>().like("name", keyword));
        model.addAttribute("foods",foods);
        model.addAttribute("msg",msg);
        return "searchfoods";
    }
    @ResponseBody
    @GetMapping("upfood")
    public Map<String,Object> upfood(@RequestParam("fid")int fid,@RequestParam("name")String name){
        System.out.println("name-=="+name);
        Food fid1 = foodService.getOne(new QueryWrapper<Food>().eq("fid", fid));
        Food name2 = foodService.getOne(new QueryWrapper<Food>().eq("name", name));
        HashMap<String, Object> map = new HashMap<>();

        String name1 = fid1.getName();
        if(name2==null){
           map.put("exist",false);
           map.put("name",fid1.getName());
        }
        else if(fid1.getName().equals(name)){
            map.put("exist",false);
            map.put("name",fid1.getName());
        }
        else {
            map.put("exist",true);
            map.put("name",fid1.getName());
        }
        return map;
    }
    @GetMapping("/updatefood")
    public String update1(@RequestParam("fid")int fid,
                          @RequestParam(value = "msg",required = false)String msg,
                          Model model,HttpSession httpSession){
        Food food = foodService.getOne(new QueryWrapper<Food>().eq("fid", fid));
        if(food.getVid()==1){
           httpSession.setAttribute("vid","zhucai");
        }
        else
            httpSession.setAttribute("vid","xiaochi");
        model.addAttribute("food",food);
        model.addAttribute("msg",msg);
        return "updatefood";
    }
    @PostMapping("/updatefood")
    public String updatefood(Food food, HttpSession httpSession,Model model,RedirectAttributes ra,
                              @RequestParam("name1")String name,@RequestParam("price1") BigDecimal price1,
                              @RequestParam(value = "file",required = false)MultipartFile file) throws IOException {
        Food one = foodService.getOne(new QueryWrapper<Food>().eq("name", food.getName()));
        //判断食品是否存在
        if(!name.equals(food.getName())&&one!=null){
            ra.addAttribute("fid",food.getFid());
            ra.addAttribute("msg","食品已存在");
            return "redirect:/updatefood";
        }
        //判断是否改了价格
        if(!price1.equals(food.getPrice())){
            foodService.update(new UpdateWrapper<Food>().eq("fid",food.getFid())
                        .set("price",food.getPrice()));
            List<Nopay> oid = nopayService.list(new QueryWrapper<Nopay>().eq("fid", food.getFid()));
            List<Order>  orders= new ArrayList<>();

            for (Nopay nopay : oid) {
                Order oid1 = orderService.getOne(new QueryWrapper<Order>().eq("oid", nopay.getOid()));
                orders.add(oid1);
            }
            System.out.println(orders.size());

            orderService.tprice(orders);
        }
        //判断是否改了图片
        if(file.getSize()!=0) {
            File dest = null;
            String fileName = file.getOriginalFilename();

            if (food.getVid() == 1) {
                dest = new File(new File(pzhucai1).getAbsolutePath() + "/" + fileName);
                food.setPicture("images/zhucai1/"+fileName);
            } else {
                dest = new File(new File(pxiaochi).getAbsolutePath() + "/" + fileName);
                food.setPicture("images/xiaochi/"+fileName);
            }

        /*if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }*/
            try {
                file.transferTo(dest); // 保存文件
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        foodService.update(new UpdateWrapper<Food>().set("name",food.getName()).set("price",food.getPrice())
        .set("des",food.getDes()).eq("fid",food.getFid()).set("picture",food.getPicture()));
      /*  foodService.updateById(food);*/
        String vid = (String) httpSession.getAttribute("vid");
        ra.addAttribute("msg","ok");
        if(vid.equals("zhucai"))
        return "redirect:/zhucai1";
        else {
            return "redirect:/xiaochi";
        }
    }
    @GetMapping("/deletefood")
    public String delete1(@RequestParam("fid")int fid){
        Food food = foodService.getOne(new QueryWrapper<Food>().eq("fid", fid));
        foodService.remove(new QueryWrapper<Food>().eq("fid", fid));

        if(food.getVid()==1){
           return "redirect:/zhucai1";
        }
        else
           return "redirect:/xiaochi";
    }
    @GetMapping("/addfood")
    public String addfood(){

        return "addfood";
    }
    @ResponseBody
    @GetMapping("adfood")
    public Map<String,Object> adfood(@RequestParam("name")String name){
        System.out.println("name-=="+name);
        Food name2 = foodService.getOne(new QueryWrapper<Food>().eq("name", name));
        HashMap<String, Object> map = new HashMap<>();
        if(name2==null){
            map.put("exist",false);
        }
        else {
            map.put("exist",true);
        }
        return map;
    }

    @PostMapping("/addfood")
    public String addfood1(@RequestParam("file")MultipartFile file,Food food,Model model){
        Food one = foodService.getOne(new QueryWrapper<Food>().eq("name", food.getName()));
        if(one!=null){
            model.addAttribute("msg","食品已存在");
            return "addfood";
        }
        if(file.getSize()!=0) {
            File dest = null;
            String fileName = file.getOriginalFilename();

            if (food.getVid() == 1) {
                dest = new File(new File(pzhucai1).getAbsolutePath() + "/" + fileName);
                food.setPicture("images/zhucai1/"+fileName);
            } else {
                dest = new File(new File(pxiaochi).getAbsolutePath() + "/" + fileName);
                food.setPicture("images/xiaochi/"+fileName);
            }
        /*if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }*/
            try {
                file.transferTo(dest); // 保存文件
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        foodService.save(food);
        return "redirect:/index.html";
    }
}
