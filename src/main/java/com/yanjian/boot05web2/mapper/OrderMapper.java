package com.yanjian.boot05web2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanjian.boot05web2.bean.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
