package com.yanjian.boot05web2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanjian.boot05web2.bean.User;
import com.yanjian.boot05web2.mapper.UserMapper;
import com.yanjian.boot05web2.service.UserService;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {

    @Override
    public boolean update1(User user,String s) {
        User one = getOne(new QueryWrapper<User>().eq("username", user.getUsername()).eq("password", s));
        if(one!=null) {
            boolean username = update(new UpdateWrapper<User>().set("password",user.getPassword()).eq("username",user.getUsername()));
            return true;
        }
        else
        return false;
    }

    @Override
    public boolean forget(String username, String telphone) {
        User one = getOne(new QueryWrapper<User>().eq("username", username).eq("telphone", telphone));
        if(one==null){
            return false;
        }else {
            return true;
        }

    }

    @Override
    public boolean reset(String username, String password) {
        boolean update = update(new UpdateWrapper<User>().set("password", password).eq("username", username));

        return update;
    }


}
