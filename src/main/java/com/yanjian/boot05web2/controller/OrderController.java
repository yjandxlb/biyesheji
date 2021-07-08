package com.yanjian.boot05web2.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.yanjian.boot05web2.bean.*;
import com.yanjian.boot05web2.config.MyAlipayClient;
import com.yanjian.boot05web2.service.*;
import com.yanjian.boot05web2.util.Alipay;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.server.ObjID;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Controller
public class OrderController {
    @Autowired
    MyAlipayClient myAlipayClient;
    @Autowired
   OrderService orderService;
    @Autowired
    NopayService nopayService;
    @Autowired
    NeworderService neworderService;
    @Autowired
    FoodService foodService;
    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;
    @Autowired
    DOrderService dOrderService;
    //转发
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private String notifulurl=" http://7m5wp5.natappfree.cc/notify";
//    ttwrok3069@sandbox.com
//正常跳转
    @GetMapping("/success")
    public synchronized String success( HttpServletRequest request, Model model,HttpSession httpSession)
            throws UnsupportedEncodingException, AlipayApiException {
        System.out.println("successss");
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        System.out.println(requestParams);
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        System.out.println(params);
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no"));

        //支付宝交易号
//        C0t469micfSYaFkfDzRG8F18z3EcLw%2FcKmXbZ5CrOQ1Wo8yeepPmMXC1z0aHD7R
        String trade_no = new String(request.getParameter("trade_no")/*.getBytes("ISO-8859-1"),"utf-8"*/);

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)

       /* boolean verify_result = AlipaySignature.rsaCheckV2(params, MyAlipayClient.alipayPubliKey,
                "utf-8", "RSA2");
        System.out.println(verify_result);*/

        Order oid1 = orderService.getOne(new QueryWrapper<Order>().eq("oid", out_trade_no));
        System.out.println(oid1);

        //创建新订单，

        Neworder neworder = new Neworder();
        if(neworderService.getOne(new QueryWrapper<Neworder>().eq("oid",out_trade_no))==null) {
           //先保存，再读取。
            neworder.setOid(Long.valueOf(out_trade_no));
            neworder.setUsername(oid1.getUsername());
            neworder.setName(oid1.getName());
            neworder.setTelphone(oid1.getTelphone());
            neworder.setAddress(oid1.getAddress());
            neworderService.save(neworder);
            simpMessagingTemplate.convertAndSend("/topic/main", "新的订单");
        }
        DesOrder desOrder=null;
        if ("0".equals(oid1.getTradeno())) {
            nopayService.remove(new QueryWrapper<Nopay>().eq("oid",out_trade_no));

           orderService.update(new UpdateWrapper<Order>().set("statuss","1").set("tradeno",trade_no).
                    eq("oid",out_trade_no));

        }
       /* else
        {
           *//* System.out.println("falifali");
            model.addAttribute("ok", "fail");
            return "redirect:/index.html";*//*
        }*/
        String total_amount = request.getParameter("total_amount");
        model.addAttribute("total", total_amount);
        model.addAttribute("oid",out_trade_no);
        System.out.println("同步通知成功");
        //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
        return "success";
    }
    @PostMapping("/notify")
    public String goumai1(HttpServletRequest request,Model model) throws AlipayApiException, UnsupportedEncodingException {

        System.out.println("goumaigoumai");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = AlipaySignature.rsaCheckV2(params, MyAlipayClient.alipayPubliKey,
                "utf-8", "RSA2");
        System.out.println(verify_result);
        //创建新订单，
        Neworder neworder = new Neworder();
        Order oid1 = orderService.getOne(new QueryWrapper<Order>().eq("oid", out_trade_no));
        //创建新订单，

        if(neworderService.getOne(new QueryWrapper<Neworder>().eq("oid",out_trade_no))==null) {
            //先保存，再读取。
            neworder.setOid(Long.valueOf(out_trade_no));
            neworder.setUsername(oid1.getUsername());
            neworder.setName(oid1.getName());
            neworder.setTelphone(oid1.getTelphone());
            neworder.setAddress(oid1.getAddress());
            neworderService.save(neworder);
            simpMessagingTemplate.convertAndSend("/topic/main", "新的订单");
        }
        DesOrder desOrder=null;
        Order oid = orderService.getOne(new QueryWrapper<Order>().eq("oid", out_trade_no));
        System.out.println(oid);

        if (oid.getTradeno().equals("0")) {
            nopayService.remove(new QueryWrapper<Nopay>().eq("oid",out_trade_no));
            orderService.update(new UpdateWrapper<Order>().set("statuss",1).set("tradeno",trade_no).
                    eq("oid",out_trade_no));
            System.out.println("1111");
            model.addAttribute("ok", "success");
            System.out.println("异步通知成功");
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            return "ok";
        }
        else
        {
            System.out.println("falils");
            model.addAttribute("ok", "fail");
            return "ok";
        }

    }

    @PostMapping("/zhifu")
    public String zhifu(@RequestParam(value = "foods", required = false) String foods,
                        @RequestParam("username") String username,
                        @RequestParam("uid") int uid,Model model) {
        ArrayList<Food> foods1 = new ArrayList<>();
        ArrayList<Integer> number = new ArrayList<>();


        System.out.println(foods);
        if (foods == null) {
            return "redirect:/gouwuche";
        }
        String[] split = foods.split(",");
        BigDecimal total=new BigDecimal("0");
        for (int i = 0; i <split.length ; i++) {

            System.out.println(split[i]);
            Food name = foodService.getOne(new QueryWrapper<Food>().eq("name", split[i]));
            foods1.add(name);
            DOrder one = dOrderService.getOne(new QueryWrapper<DOrder>().eq("fid", name.getFid()).eq("uid", uid));
            System.out.println(one.getFnumber());
            number.add(one.getFnumber());
            System.out.println(number.get(i));
            total = total.add(name.getPrice().multiply(BigDecimal.valueOf(one.getFnumber())));
        }
        List<Address> uid1 = addressService.list(new QueryWrapper<Address>().eq("uid", uid).orderByDesc("pri"));
        model.addAttribute("addresses",uid1);
        model.addAttribute("total",total);
        model.addAttribute("food",foods1);
        model.addAttribute("number",number);
        model.addAttribute("ty","2");
        return "readgoumai";
    }


    @PostMapping("/zhifu1")
    public String zhifu1(@RequestParam(value = "foods", required = false) String foods,
                        @RequestParam("username") String username,
                        @RequestParam("uid") int uid,
                         @RequestParam("address")String aid, Model model) {
        System.out.println(foods);
        if (foods == null) {
            return "redirect:/gouwuche";
        }

        Long oid = IdUtil.createSnowflake(0, 1).nextId();

        BigDecimal zhifu = orderService.zhifu(oid, foods, username, uid,aid);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl("http://localhost:8080/success");
        alipayRequest.setNotifyUrl(notifulurl);
//        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest ();
        alipayRequest.setBizContent("{" +
//                "    \"out_trade_no\":\"20150320010101002321221\","  +//订单号要唯一
                "    \"out_trade_no\":" + oid + "," +//订单号要唯一
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + zhifu +"," +
                "    \"subject\":\"食物\"," +
                "    \"body\":\"513餐馆\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207856\"" +
                "    }" +
                "  }"); //填充业务参数

        /*subject：必填，商品的标题/交易标题/订单标题/订单关键字等。 不可使用特殊字符，如 /,=,& 等。

        product_code：必填，销售产品码，与支付宝签约的产品码名称。目前电脑支付场景下仅支持 FAST_INSTANT_TRADE_PAY。

        total_amount：必填，订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。金额不能为0。*/

        String body = null;
        try {
            body = MyAlipayClient.alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(body);
        model.addAttribute("ok",body);

        return "ok";

    }
    @PostMapping("/goumai")
    public String goumai(
            @RequestParam("fname") String food
            , @RequestParam("uid") String uid,
            @RequestParam("fnumber") int fnumber,Model model,
            HttpServletRequest request1, HttpServletResponse response1){
        Food name = foodService.getOne(new QueryWrapper<Food>().eq("name", food));
    //地址
        List<Address> uid1 = addressService.list(new QueryWrapper<Address>().eq("uid", uid).orderByDesc("pri"));
        model.addAttribute("addresses",uid1);
        model.addAttribute("total",name.getPrice().multiply(BigDecimal.valueOf(fnumber)));
        model.addAttribute("food",name);
        model.addAttribute("number",fnumber);
        model.addAttribute("ty","1");
        return "readgoumai";

    }
    @PostMapping("/goumai1")
    public void goumai1(
            @RequestParam("fname") String food
            , @RequestParam("username") String username,
                       @RequestParam("fnumber") int fnumber,
                       @RequestParam("address")String aid,
                       HttpServletRequest request1, HttpServletResponse response1) throws IOException {

        if(fnumber==0){
            response1.setContentType("text/html;charset=utf-8");
            response1.getWriter().write("数量不能为0");

            response1.getWriter().flush();
            response1.getWriter().close();

        }
        else {
            Long oid = IdUtil.createSnowflake(0, 1).nextId();
            BigDecimal goumai = orderService.goumai(oid, food, username, fnumber,aid);

            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl("http://localhost:8080/success");
            alipayRequest.setNotifyUrl(notifulurl);
//        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest ();
            alipayRequest.setBizContent("{" +
//                "    \"out_trade_no\":\"20150320010101002321221\","  +//订单号要唯一
                    "    \"out_trade_no\":"+oid+"," +//订单号要唯一
                    "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                    "    \"total_amount\":" + goumai + "," +
                    "    \"subject\":\"支付\"," +
                    "    \"body\":\"513餐馆\"," +
                    "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                    "    \"extend_params\":{" +
                    "    \"sys_service_provider_id\":\"2088511833207856\"" +
                    "    }" +
                    "  }"); //填充业务参数

        /*subject：必填，商品的标题/交易标题/订单标题/订单关键字等。 不可使用特殊字符，如 /,=,& 等。

        product_code：必填，销售产品码，与支付宝签约的产品码名称。目前电脑支付场景下仅支持 FAST_INSTANT_TRADE_PAY。

        total_amount：必填，订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。金额不能为0。*/
            System.out.println("1");
            String body = null;
            try {
                body = MyAlipayClient.alipayClient.pageExecute(alipayRequest).getBody();
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            System.out.println(body);
            response1.setContentType("text/html;charset=utf-8");

            response1.getWriter().write(body);//直接将完整的表单html输出到页面
            response1.getWriter().flush();
            response1.getWriter().close();
            System.out.println("2");
        }
    }



    @GetMapping("/fukuan")
    public String fukuan(@RequestParam("oid")Long oid,HttpServletResponse response1,Model model,HttpSession httpSession) throws IOException, AlipayApiException {
        User loginUser = (User) httpSession.getAttribute("loginUser");
        List<Nopay> oid1 = nopayService.list(new QueryWrapper<Nopay>().eq("oid", oid));
        ArrayList<Food> foods = new ArrayList<>();
        ArrayList<Integer> number = new ArrayList<>();
        BigDecimal total=new BigDecimal("0");
        for (Nopay nopay : oid1) {
            Food fid = foodService.getOne(new QueryWrapper<Food>().eq("fid", nopay.getFid()));
            foods.add(fid);
            number.add(nopay.getFnumber());
            total=total.add(fid.getPrice().multiply(BigDecimal.valueOf(nopay.getFnumber())));
        }
        List<Address> uid1 = addressService.list(new QueryWrapper<Address>().eq("uid", loginUser.getUid())
                .orderByDesc("pri"));
        model.addAttribute("addresses",uid1);
        model.addAttribute("total",total);
        model.addAttribute("food",foods);
        model.addAttribute("number",number);
        model.addAttribute("oid",oid);
        model.addAttribute("ty","3");
        return "readgoumai";
    }
    //未付款订单付款
   @ResponseBody
    @PostMapping("/fukuan1")
    public void fukuan1(@RequestParam("oid")Long oid,HttpServletResponse response1,
                        @RequestParam("address")Integer aid) throws IOException, AlipayApiException {
       Address aid1 = addressService.getOne(new QueryWrapper<Address>().eq("aid", aid));
       Order oid1 = orderService.getOne(new QueryWrapper<Order>().eq("oid", oid));
       boolean update = orderService.update(new UpdateWrapper<Order>().eq("oid", oid).
               set("address", aid1.getAddress()).set("name",aid1.getName()).set("telphone",aid1.getTelphone()));
       System.out.println(oid1);
    BigDecimal tprice = oid1.getTprice();
       System.out.println(tprice);
       AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
       alipayRequest.setReturnUrl("http://localhost:8080/success");
       alipayRequest.setNotifyUrl(notifulurl);
//        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest ();
       alipayRequest.setBizContent("{" +
//                "    \"out_trade_no\":\"20150320010101002321221\","  +//订单号要唯一
               "    \"out_trade_no\":" + oid + "," +//订单号要唯一
               "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
               "    \"total_amount\":" +tprice + "," +
               "    \"subject\":\"食物\"," +
               "    \"body\":\"513餐馆\"," +
               "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
               "    \"extend_params\":{" +
               "    \"sys_service_provider_id\":\"2088511833207856\"" +
               "    }" +
               "  }"); //填充业务参数

        /*subject：必填，商品的标题/交易标题/订单标题/订单关键字等。 不可使用特殊字符，如 /,=,& 等。

        product_code：必填，销售产品码，与支付宝签约的产品码名称。目前电脑支付场景下仅支持 FAST_INSTANT_TRADE_PAY。

        total_amount：必填，订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。金额不能为0。*/
       System.out.println("1");
       String body = null;
       try {
           body = MyAlipayClient.alipayClient.pageExecute(alipayRequest).getBody();
       } catch (AlipayApiException e) {
           e.printStackTrace();
       }
       response1.setContentType("text/html;charset=utf-8");

       response1.getWriter().write(body);//直接将完整的表单html输出到页面
       response1.getWriter().flush();
       response1.getWriter().close();
       System.out.println("2");

    }

        //查找未付款
    @GetMapping("/unorder")
    public String unorder(@RequestParam(value = "kname", defaultValue = "") String kname,
                          @RequestParam(value = "kdate", defaultValue = "") String kdate,
                          @RequestParam(value = "koid", defaultValue = "") Long koid,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          Model model, HttpSession session){

        System.out.println("kname" + kname + "kdate++" + kdate + "koid==" + koid);
        User loginUser = (User) session.getAttribute("loginUser");
        Page<Order> page1 = new Page<>(page, 5);
        Page<Order> orderPage = null;
        if (loginUser.getAdm() == 1) {
            orderPage = orderService.searchno(kname, koid, kdate, page);

        } else {
            orderPage = orderService.searchno(loginUser.getUsername(), koid, kdate, page);
        }
    /*
        page2.getCurrent();
        page2.getPages();//总页数
        page2.getTotal();//总条数*/
//        List<Order> records = page2.getRecords();
        model.addAttribute("orders", orderPage);
        model.addAttribute("kname", kname);
        model.addAttribute("kdate", kdate);
        model.addAttribute("koid", koid);
        return "unorder";
    }
    @GetMapping("/unorder/delete/{oid}")
    public String unorderDelete(@PathVariable("oid") Long oid,
                         @RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "kname", required = false) String kname,
                         @RequestParam(value = "kdate", required = false) String kdate,
                         @RequestParam(value = "koid", required = false) Long koid,
                         RedirectAttributes ra) {
        orderService.remove(new QueryWrapper<Order>().eq("oid", oid));
        ra.addAttribute("page", page);
        ra.addAttribute("kname", kname);
        ra.addAttribute("kdate", kdate);
        ra.addAttribute("koid", koid);
        return "redirect:/unorder";
    }

        //查找付款
    @GetMapping("/order")
    public String gouwuche3(@RequestParam(value = "kname", defaultValue = "") String kname,
                            @RequestParam(value = "kdate", defaultValue = "") String kdate,
                            @RequestParam(value = "koid", defaultValue = "") Long koid,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            Model model, HttpSession session) {
        System.out.println("kname" + kname + "kdate++" + kdate + "koid==" + koid);
        User loginUser = (User) session.getAttribute("loginUser");
        Page<Order> page1 = new Page<>(page, 5);
        Page<Order> orderPage = null;
        if (loginUser.getAdm() == 1) {
            orderPage = orderService.searchAll(kname, koid, kdate, page);

        } else {
            orderPage = orderService.searchAll(loginUser.getUsername(), koid, kdate, page);
        }
    /*
        page2.getCurrent();
        page2.getPages();//总页数
        page2.getTotal();//总条数*/
//        List<Order> records = page2.getRecords();
        model.addAttribute("orders", orderPage);
        model.addAttribute("kname", kname);
        model.addAttribute("kdate", kdate);
        model.addAttribute("koid", koid);
        return "order";
    }

    @GetMapping("/order/delete/{oid}")
    public String delete(@PathVariable("oid") Long oid,
                         @RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "kname", required = false) String kname,
                         @RequestParam(value = "kdate", required = false) String kdate,
                         @RequestParam(value = "koid", required = false) Long koid,
                         RedirectAttributes ra) {
        orderService.remove(new QueryWrapper<Order>().eq("oid", oid));
        ra.addAttribute("page", page);
        ra.addAttribute("kname", kname);
        ra.addAttribute("kdate", kdate);
        ra.addAttribute("koid", koid);
        return "redirect:/order";
    }
  /*@PostMapping("/search/order")
    public String searchOrder(@RequestParam(value = "kname",required = false)String kname,
                              @RequestParam(value = "kdate",required = false)String kdate,
                              @RequestParam(value = "koid",required = false)Long koid,
                              @RequestParam(value = "page",defaultValue = "1") int page,
                              HttpSession session, Model model ){
      System.out.println("kname"+kname+"kdate++"+kdate+"koid=="+koid);
      User loginUser = (User) session.getAttribute("loginUser");
      Page<Order> orderPage=null;
      if(loginUser.getAdm()==1){
          orderPage = orderService.searchAll(kname, koid, kdate, page);
      }
      else {
          orderPage = orderService.searchAll(loginUser.getUsername(), koid, kdate, page);
      }
        model.addAttribute("orders",orderPage);
        return "order";
  }*/
}
