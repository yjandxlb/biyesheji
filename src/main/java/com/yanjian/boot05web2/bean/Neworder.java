package com.yanjian.boot05web2.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_neworder")
public class Neworder {
    private Long oid;
    private String acc;
    private String suc;
    private String username;
    private String address;
    private String telphone;
    private String name;

}
