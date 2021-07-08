package com.yanjian.boot05web2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanjian.boot05web2.bean.Order;
import com.yanjian.boot05web2.bean.Total;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService extends IService<Order> {
   BigDecimal zhifu(Long oid,String s, String username, int uid,String address);

    Page<Order> searchAll(String username, Long oid, String odate, int page);

    Page<Order> searchno(String username, Long oid, String odate, int page);

   BigDecimal goumai(Long oid, String fname, String username, int number,String address);
   void tprice(List<Order> orders);

}