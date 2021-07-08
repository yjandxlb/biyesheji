package com.yanjian.boot05web2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanjian.boot05web2.bean.DesOrder;
import com.yanjian.boot05web2.bean.Order;
import com.yanjian.boot05web2.bean.Total;
import com.yanjian.boot05web2.service.DesOrderService;
import com.yanjian.boot05web2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class DesOrderController {
    @Autowired
    DesOrderService desOrderService;
    @Autowired
    OrderService orderService;
    @GetMapping("/desorder")
    public String getByOid(@RequestParam("oid")Long oid, Model model){
        List<DesOrder> orders = desOrderService.list(new QueryWrapper<DesOrder>().eq("oid", oid));
        Map<String, Object> tmaps = desOrderService.getMap(new QueryWrapper<DesOrder>().
                eq("oid", oid).select("sum(tprice) as tprice"));
        Double ttprice = (Double) tmaps.get("tprice");
        model.addAttribute("ttprice",ttprice);
        model.addAttribute("orders",orders);
        return "desorder";
    }

    @GetMapping("/total")
    public String total(@RequestParam(value = "kdate",required = false)String date,Model model){
        System.out.println(date);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format=null;
        if(date==null||"".equals(date)){

          format = simpleDateFormat.format(new Date());
            System.out.println(format);

        }
        else {
           format=date;
        }
        List<Total> all = desOrderService.getAll(format);
        System.out.println(all);
        String s1=format+" 00:00:00";
        String s2=format+" 23:59:59";
        Map<String, Object> map = orderService.getMap(new QueryWrapper<Order>().
                ge("odate", s1).le("odate", s2).eq("statuss",1).select("sum(tprice) as tprice"));
       Double tprice = (Double) map.get("tprice");
        System.out.println(tprice);
        model.addAttribute("tprice",tprice);
        model.addAttribute("totals",all);
        return "total";
    }
}
