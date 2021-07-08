package com.yanjian.boot05web2.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_address")
public class Address {
    private Integer aid;
    private Integer uid;
    private String address;
    private String name;
    private String telphone;
    private Integer pri;

}

