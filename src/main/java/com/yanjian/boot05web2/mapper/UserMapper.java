package com.yanjian.boot05web2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanjian.boot05web2.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
