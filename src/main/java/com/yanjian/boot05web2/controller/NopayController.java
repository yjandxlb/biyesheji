package com.yanjian.boot05web2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanjian.boot05web2.bean.DesOrder;
import com.yanjian.boot05web2.bean.Food;
import com.yanjian.boot05web2.bean.Nopay;
import com.yanjian.boot05web2.service.FoodService;
import com.yanjian.boot05web2.service.NopayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class NopayController {
    @Autowired
    NopayService nopayService;
    @Autowired
    FoodService foodService;
    @GetMapping("desnopay")
    public String desnopay(@RequestParam("oid")Long oid, Model model){
        List<Nopay> oid1 = nopayService.list(new QueryWrapper<Nopay>().eq("oid", oid));
        /*Map<String, Object> tmaps =nopayService.getMap(new QueryWrapper<Nopay>().
                eq("oid", oid).select("sum(tprice) as tprice"));
        BigDecimal ttprice = (BigDecimal) tmaps.get("tprice");*/
        List<Food> foods=new ArrayList<>();

        //构造时需要传个字符串，否则也认为是double精度。
        BigDecimal tprice= new BigDecimal("0");
        for (Nopay nopay : oid1) {
            Food fid = foodService.getOne(new QueryWrapper<Food>().eq("fid", nopay.getFid()));
            tprice=tprice.add(fid.getPrice().multiply(BigDecimal.valueOf(nopay.getFnumber())));
            foods.add(fid);
        }
        model.addAttribute("tprice",tprice);
        model.addAttribute("orders",oid1);
        model.addAttribute("foods",foods);
        return "nopaydesorder";
    }
}
