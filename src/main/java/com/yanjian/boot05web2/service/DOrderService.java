package com.yanjian.boot05web2.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yanjian.boot05web2.bean.DOrder;

public interface DOrderService extends IService<DOrder> {
    void savea1(DOrder dOrder);
    void add1(int fid,int uid);
    void minus1(int fid,int uid);
}
