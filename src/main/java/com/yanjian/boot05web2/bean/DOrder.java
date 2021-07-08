package com.yanjian.boot05web2.bean;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("t_dorder")
public class DOrder  {

        private Integer gid;
        private Integer fid;
        private Integer fnumber;
        private Integer uid;

}
