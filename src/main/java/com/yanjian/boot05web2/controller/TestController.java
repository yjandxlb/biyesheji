package com.yanjian.boot05web2.controller;

import cn.hutool.core.lang.func.VoidFunc;
import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.yanjian.boot05web2.config.MyAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TestController {

    @ResponseBody
    @GetMapping("/test")
    public void test1(HttpServletRequest request1, HttpServletResponse response1) throws IOException, AlipayApiException {
        AlipayClient alipayClient = MyAlipayClient.alipayClient;

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        Long oid = IdUtil.createSnowflake(0, 1).nextId();
        alipayRequest.setReturnUrl("http://localhost:8080/buy?oid="+oid);
//        alipayRequest.setNotifyUrl("http://localhost:8080/success");
//        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest ();
        alipayRequest.setBizContent( "{"  +
//                "    \"out_trade_no\":\"20150320010101002321221\","  +//订单号要唯一
                "    \"out_trade_no\":"+oid+","  +//订单号要唯一
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\","  +
                "    \"total_amount\":20.10,"  +
                "    \"subject\":\"Iphone6 32G\","  +
                "    \"body\":\"Iphone6 16G\","  +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","  +
                "    \"extend_params\":{"  +
                "    \"sys_service_provider_id\":\"2088511833207856\""  +
                "    }" +
                "  }" ); //填充业务参数

        /*subject：必填，商品的标题/交易标题/订单标题/订单关键字等。 不可使用特殊字符，如 /,=,& 等。

        product_code：必填，销售产品码，与支付宝签约的产品码名称。目前电脑支付场景下仅支持 FAST_INSTANT_TRADE_PAY。

        total_amount：必填，订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。金额不能为0。*/

       /* AlipayTradePrecreateResponse response = null;

        {
            try {
                response = alipayClient.pageExecute(re)
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        }*/

      /*  AlipayTradePagePayResponse response=alipayClient.pageExecute(alipayRequest);*/

        String body=null;
        try {
            body = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
      /*  System.out.print(response.getBody());
*/
        // 根据response中的结果继续业务逻辑处理

        System.out.print(body);
        response1.setContentType("text/html;charset=utf-8");

        response1.getWriter().write(body);//直接将完整的表单html输出到页面
        response1.getWriter().flush();
        response1.getWriter().close();

    }


    @GetMapping("/test1")
    public String test1(){
        return "test1";
    }
}
