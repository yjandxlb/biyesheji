package com.yanjian.boot05web2.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_nopay")
public class Nopay {
    private Long oid;
    private Integer fid;
    private Integer fnumber;

}
