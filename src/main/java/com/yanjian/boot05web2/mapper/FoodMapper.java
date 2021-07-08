package com.yanjian.boot05web2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanjian.boot05web2.bean.Food;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface FoodMapper  extends BaseMapper<Food> {
}
