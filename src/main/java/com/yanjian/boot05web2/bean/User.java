package com.yanjian.boot05web2.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("t_user")
public class User {
    private Integer uid;
    private String username;
    private String password;
    private String email;
    private String sex;
    private Integer adm;
    private String telphone;
}
