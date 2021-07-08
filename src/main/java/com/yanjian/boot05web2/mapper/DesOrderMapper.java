package com.yanjian.boot05web2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanjian.boot05web2.bean.DesOrder;
import com.yanjian.boot05web2.bean.Total;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DesOrderMapper  extends BaseMapper<DesOrder> {
  /*  SELECT SUM(fnumber) total,des.`fname`  FROM t_desorder des,t_order o WHERE o.`odate`>='2021-05-24 00:00:00'
    AND o.`odate`<='2021-05-24 23:59:59' AND o.`statuss`='1' AND  o.`oid`=des.`oid`
    GROUP BY des.`fname` ORDER BY total DESC*/
    @Select("SELECT sum(fnumber) total,des.`fname`  FROM t_desorder des,t_order o WHERE o.`odate`>=#{s1} " +
            "AND o.`odate`<=#{s2}  AND o.`statuss`='1' AND  o.`oid`=des.`oid` " +
            "GROUP BY des.`fname` ORDER BY total DESC")
        List<Total> getAll(String s1,String s2);


}
