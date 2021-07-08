package com.yanjian.boot05web2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanjian.boot05web2.bean.DesOrder;
import com.yanjian.boot05web2.bean.Total;

import java.util.List;

public interface DesOrderService extends IService<DesOrder> {
    List<Total> getAll(String s);
}
