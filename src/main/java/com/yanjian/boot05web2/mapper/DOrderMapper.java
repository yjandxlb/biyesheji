package com.yanjian.boot05web2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanjian.boot05web2.bean.DOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface DOrderMapper extends BaseMapper<DOrder> {
}
