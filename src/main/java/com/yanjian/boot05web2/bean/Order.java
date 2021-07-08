package com.yanjian.boot05web2.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Date;
@Data
@TableName("t_order")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long oid;
    private String username;
    private BigDecimal tprice;
    private String   odate;
    private Integer statuss;
    private String  tradeno;
    private String address;
    private String name;
    private String telphone;

}
