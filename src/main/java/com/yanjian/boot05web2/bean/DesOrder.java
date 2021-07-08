package com.yanjian.boot05web2.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@TableName("t_desorder")
@AllArgsConstructor
@NoArgsConstructor
public class DesOrder {
    private Long oid;
    private String fname;
    private String username;
    private BigDecimal fprice;
    private String fpicture;
    private BigDecimal tprice;
    private Integer fnumber;
}
