package com.yanjian.boot05web2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.yanjian.boot05web2.bean.DesOrder;
import com.yanjian.boot05web2.bean.Total;
import com.yanjian.boot05web2.mapper.DesOrderMapper;
import com.yanjian.boot05web2.service.DesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DesOrderServiceImpl extends ServiceImpl<DesOrderMapper, DesOrder> implements DesOrderService {
    @Autowired
    DesOrderMapper desOrderMapper;
    @Override
    public List<Total> getAll(String format) {

        String s1=format+" 00:00:00";
        String s2=format+" 23:59:59";
        System.out.println("s1="+s1+"s2="+s2);
        List<Total> all = desOrderMapper.getAll(s1, s2);
        return all;
    }
}
