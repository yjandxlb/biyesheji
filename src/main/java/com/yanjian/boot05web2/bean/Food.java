package com.yanjian.boot05web2.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_food")
public class Food {
    private Integer fid;
    private Integer vid;
    private String name;
    private String picture;
    private String des;
    private BigDecimal price;
    private Integer number;
}
