package com.yanjian.boot05web2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanjian.boot05web2.bean.User;

public interface UserService extends IService<User> {
    boolean update1(User user,String s);
    //查看是否用户名和手机号对应，
    boolean forget(String username,String telphone);
    boolean reset(String username,String password);

}
