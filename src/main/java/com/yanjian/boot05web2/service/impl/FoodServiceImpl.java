package com.yanjian.boot05web2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanjian.boot05web2.bean.Food;
import com.yanjian.boot05web2.mapper.FoodMapper;
import com.yanjian.boot05web2.service.FoodService;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {
}
