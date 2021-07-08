package com.yanjian.boot05web2.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yanjian.boot05web2.bean.DOrder;
import com.yanjian.boot05web2.bean.Food;
import com.yanjian.boot05web2.mapper.DOrderMapper;
import com.yanjian.boot05web2.service.DOrderService;
import com.yanjian.boot05web2.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DOrderServiceImpl extends ServiceImpl<DOrderMapper, DOrder> implements DOrderService {
    @Autowired
    FoodService foodService;
        //加入购物车
    @Override
    public void savea1(DOrder desOrder) {
       foodService.getOne(new QueryWrapper<Food>().eq("fid",desOrder.getFid()));
        Integer uid = desOrder.getUid();
        Integer fnumber = desOrder.getFnumber();

       DOrder fname1 = getOne(new QueryWrapper<DOrder>().
               eq("fid", desOrder.getFid()).eq("uid",uid));

        if(fname1!=null){
           update(new UpdateWrapper<DOrder>().
                   set("fnumber",fname1.getFnumber()+fnumber).
                   eq("gid",fname1.getGid()));
        }
        else {
            boolean save = save(desOrder);
            System.out.println(save);
        }
    }

    @Override
    public void add1(int fid,int uid) {
       DOrder fid1 = getOne(new QueryWrapper<DOrder>().eq("fid",fid).eq("uid",uid));

        update(new UpdateWrapper<DOrder>().set("fnumber",fid1.getFnumber()+1).
                eq("gid",fid1.getGid()));
    }
    public void minus1(int fid,int uid) {
      DOrder fid1 = getOne(new QueryWrapper<DOrder>().eq("fid",fid).eq("uid",uid));
        if(fid1.getFnumber()>1)
        update(new UpdateWrapper<DOrder>().set("fnumber",fid1.getFnumber()-1).eq("gid",fid1.getGid()));
    }
}
