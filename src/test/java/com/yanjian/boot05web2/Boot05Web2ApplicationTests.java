package com.yanjian.boot05web2;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alipay.api.domain.Person;
import com.alipay.api.domain.SpiDetectionTask;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanjian.boot05web2.bean.Food;
import com.yanjian.boot05web2.config.SendSms;
import com.yanjian.boot05web2.service.FoodService;
import org.bouncycastle.math.ec.custom.sec.SecT113Field;
import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;


@SpringBootTest
class Boot05Web2ApplicationTests {

@Autowired
FoodService foodService;


    @Test
    void contextLoads() {
        String s = IdUtil.fastSimpleUUID();
        System.out.println(s);
        String s1 = IdUtil.fastUUID();
        System.out.println(s1);
        Snowflake snowflake = IdUtil.createSnowflake(0, 1);
        System.out.println(snowflake.nextId());
        java.util.Date date = new java.util.Date();
        long time = date.getTime();
        System.out.println(date);
         Date date1 = new java.sql.Date(new java.util.Date().getTime());
        System.out.println(date1);
        System.out.println("******");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //从前端或者自己模拟一个日期格式，转为String即可
        String format1 = format.format(date);
        System.out.println(format1);


    }
    @Test
    void test2(){
        Food byId = foodService.getOne(new QueryWrapper<Food>().eq("fid",9));
        System.out.println(byId);
    }
    @Test
    void aVoid12() {
        for (int i = 0; i < 10; i++) {
            int s = RandomUtil.randomInt(1000,10000);
            System.out.println(s);
        }


    }



    @Test
    void aVoid(int a) {
        {
            System.out.println("个位数为：" + a % 10);
            System.out.println("十位数为：" + a / 10 % 10);
            System.out.println("百位数为：" + a / 10 / 10);


        }
    }
    @Test
    void aVoid1() {
        {

            Integer a=new Integer(3);
            Integer b=3;
            int c=3;
            System.out.println(a==b);
            System.out.println(a==c);
            String d="12";
            double e=10d;
            String f=d+e;
            System.out.println(f);
        }
    }


    @Test
    void mergeFiles(String outFile, String[] files, Integer bufSize) throws IOException, Exception {
        if (bufSize == null) {
            bufSize = 1;
        }
        FileChannel outChannel = null;
        outChannel = new FileOutputStream(outFile, true).getChannel();
        for (String f : files) {
            FileChannel fc = new FileInputStream(f).getChannel();
            ByteBuffer bb = ByteBuffer.allocate(bufSize);
            while (fc.read(bb) != -1) {
                bb.flip();
                outChannel.write(bb);
                bb.clear();
            }
            fc.close();
        }
        System.out.println("Merged!! ");
        outChannel.close();
    }

}




