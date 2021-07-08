package com.yanjian.boot05web2.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanjian.boot05web2.bean.*;
import com.yanjian.boot05web2.config.MyAlipayClient;
import com.yanjian.boot05web2.mapper.OrderMapper;
import com.yanjian.boot05web2.service.*;
import com.yanjian.boot05web2.util.Alipay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService {
    @Autowired
    DesOrderService desOrderService;
    @Autowired
    DOrderService dOrderService;
    @Autowired
    FoodService foodService;
    @Autowired
    MyAlipayClient myAlipayClient;
    @Autowired
    NopayService nopayService;
    @Autowired
    AddressService addressService;
    private Lock lock=new ReentrantLock();

  public BigDecimal zhifu(Long oid,String s,String username,int uid,String aid){
      //得到收货地址
      Address aid1 = addressService.getOne(new QueryWrapper<Address>().eq("aid", aid));
      SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

     String format = simpleDateFormat.format(new Date());
     BigDecimal tprice=new BigDecimal(0);
     //创建order对象；
      Order order=new Order();
      order.setOid(oid); order.setOdate(format);
      order.setUsername(username); order.setAddress(aid1.getAddress());
      order.setName(aid1.getName()); order.setTelphone(aid1.getTelphone());
      //先要保存到主表中，
      save(order);
      //创建nopay对象；
      Nopay nopay=new Nopay();
      DesOrder desOrder=new DesOrder();

      String[] split = s.split(",");
      for (String s1 : split) {
          //找到购物车的相关食物
          Food name = foodService.getOne(new QueryWrapper<Food>().eq("name", s1));
          DOrder one = dOrderService.getOne(new QueryWrapper<DOrder>().eq("fid", name.getFid())
                  .eq("uid",uid));
          dOrderService.remove(new QueryWrapper<DOrder>().eq("fid", name.getFid())
                  .eq("uid",uid));
          System.out.println("one++++"+one);
          //食物总量增加

          try {
              lock.lock();
              foodService.update(new UpdateWrapper<Food>().set("number",one.getFnumber()+name.getNumber())
                      .eq("fid",name.getFid()));
          } catch (Exception e) {
              e.printStackTrace();
          } finally {
              lock.unlock();
          }
          //创建详细订单
         desOrder.setOid(oid); desOrder.setFname(name.getName());
          desOrder.setFnumber(one.getFnumber());  desOrder.setUsername(username);
          desOrder.setFprice(name.getPrice());  desOrder.setTprice(name.getPrice().
                  multiply(BigDecimal.valueOf(one.getFnumber())));
         desOrder.setFpicture(name.getPicture());
          System.out.println(desOrder);
        //由于设置了外键，所以主表中先要有此oid。
          boolean save = desOrderService.save(desOrder);
          System.out.println(save);

          tprice=name.getPrice().multiply(BigDecimal.valueOf(one.getFnumber())).add(tprice);
          //保存nopay；
        nopay.setOid(oid); nopay.setFid(name.getFid()); nopay.setFnumber(one.getFnumber());
        nopayService.save(nopay);
          System.out.println(tprice);
      }
    update(new UpdateWrapper<Order>().eq("oid",oid).set("tprice",tprice));
      return tprice;
    }
    public BigDecimal goumai(Long oid,String fname,String username,int number,String aid){

        Address aid1 = addressService.getOne(new QueryWrapper<Address>().eq("aid", aid));
      //得到购买的食物
        Food one = foodService.getOne(new QueryWrapper<Food>().eq("name", fname));
        try {
            lock.lock();
            foodService.update(new UpdateWrapper<Food>().set("number",one.getNumber()+number)
                    .eq("fid",one.getFid()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        /*  Long oid = IdUtil.createSnowflake(0, 1).nextId();*/
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        BigDecimal tfrice=one.getPrice().multiply(new BigDecimal(number));
        //创建order对象；
        Order order=new Order();
        order.setOid(oid); order.setOdate(format);
        order.setUsername(username); order.setAddress(aid1.getAddress());
        order.setName(aid1.getName()); order.setTelphone(aid1.getTelphone());
        order.setTprice(one.getPrice().multiply(new BigDecimal(number)));
        System.out.println(order);
        //保存
            save(order);

        //创建nopay对象详细；
        Nopay nopay=new Nopay();
        //保存nopay；
        nopay.setOid(oid); nopay.setFid(one.getFid()); nopay.setFnumber(number);
        nopayService.save(nopay);
        //创建desorder对象详细
        DesOrder desOrder=new DesOrder();
        desOrder.setOid(oid); desOrder.setFname(fname);
        desOrder.setFnumber(number);  desOrder.setUsername(username);
        desOrder.setFprice(one.getPrice());  desOrder.setTprice(tfrice);
        desOrder.setFpicture(one.getPicture());
        desOrderService.save(desOrder);

//        String body= Alipay.getString(oid, one.getPrice(), number);

        return tfrice;
    }
        //改变未完成订单的总价钱。
    @Override
    public void tprice(List<Order> orders) {
        for (int i = 0; i < orders.size(); i++) {
            List<Nopay> oid = nopayService.list(new QueryWrapper<Nopay>().
                    eq("oid", orders.get(i).getOid()));
            BigDecimal tprice= new BigDecimal("0");
            for (Nopay nopay : oid) {
                Food fid = foodService.getOne(new QueryWrapper<Food>().eq("fid", nopay.getFid()));
                tprice=tprice.add(fid.getPrice().multiply(BigDecimal.valueOf(nopay.getFnumber())));
            }
            update(new UpdateWrapper<Order>().set("tprice",tprice).
                    eq("oid",orders.get(i).getOid()));
        }

    }
//查找付款
    @Override
    public Page<Order> searchAll(String username, Long oid, String odate,int page) {
        Page<Order> page1=new Page<>(page,5);
        QueryWrapper<Order> qw= new QueryWrapper<Order>();
        Page<Order> page2=null;
        if(!"".equals(username)&&oid!=null&&!"".equals(odate)){
                page2= page(page1,qw.eq("username",username).
                        like("oid",oid).eq("odate",odate).eq("statuss","1"));
        }
        else if(!"".equals(username)&&oid!=null)
        {
            page2=  page(page1,qw.eq("username",username).
                    like("oid",oid).eq("statuss","1"));
        }
        else if(!"".equals(username)&&!"".equals(odate))
        {
            page2= page(page1,qw.eq("username",username).
                    eq("odate",odate).eq("statuss","1"));
        }
        else if(!"".equals(odate)&&oid!=null)
        {
            page2=  page(page1,qw.eq("odate",odate).
                    like("oid",oid).eq("statuss","1"));
        }
        else if(!"".equals(odate))
        {
            page2=   page(page1,qw.eq("odate",odate).eq("statuss","1"));
        }
        else if(oid!=null)
        {
            page2=  page(page1,qw.like("oid",oid).eq("statuss","1"));
        }
        else if(!"".equals(username))
        {
            page2=  page(page1,qw.eq("username",username).eq("statuss","1"));
        }
        else
            page2=page(page1,qw.eq("statuss","1"));
        return page2;
    }
    //查找未付款
    @Override
    public Page<Order> searchno(String username, Long oid, String odate,int page) {
        System.out.println(username+oid+odate+page);
        Page<Order> page1=new Page<>(page,5);
        QueryWrapper<Order> qw= new QueryWrapper<Order>();
        Page<Order> page2=null;
        if(!"".equals(username)&&oid!=null&&!"".equals(odate)){
            page2= page(page1,qw.eq("username",username).
                    like("oid",oid).eq("odate",odate).eq("statuss","0"));
        }
        else if(!"".equals(username)&&oid!=null)
        {
            page2=  page(page1,qw.eq("username",username).
                    like("oid",oid).eq("statuss","0"));
        }
        else if(!"".equals(username)&&!"".equals(odate))
        {
            page2= page(page1,qw.eq("username",username).
                    eq("odate",odate).eq("statuss","0"));
        }
        else if(!"".equals(odate)&&oid!=null)
        {
            page2=  page(page1,qw.eq("odate",odate).
                    like("oid",oid).eq("statuss","0"));
        }
        else if(!"".equals(odate))
        {
            page2=   page(page1,qw.eq("odate",odate).eq("statuss","0"));
        }
        else if(oid!=null)
        {
            page2=  page(page1,qw.like("oid",oid).eq("statuss","0"));
        }
        else if(!"".equals(username))
        {
            page2=  page(page1,qw.eq("username",username).eq("statuss","0"));
        }
        else
            page2=page(page1,qw.eq("statuss","0"));
        return page2;
    }


}
