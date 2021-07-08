package com.yanjian.boot05web2.util;

import com.alipay.api.AbstractAlipayClient;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.yanjian.boot05web2.config.MyAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;

public class Alipay {


    public static String  getString(Long oid,Double price,int number){
        AlipayClient alipayClient = MyAlipayClient.alipayClient;
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        alipayRequest.setReturnUrl("http://localhost:8080/success?oid="+oid);

//        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest ();
        alipayRequest.setBizContent( "{"  +
//                "    \"out_trade_no\":\"20150320010101002321221\","  +//订单号要唯一
                "    \"out_trade_no\":"+oid+","  +//订单号要唯一
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\","  +
                "    \"total_amount\":"+price*number+","  +
                "    \"subject\":\"食物\","  +
                "    \"body\":\"513餐馆\","  +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","  +
                "    \"extend_params\":{"  +
                "    \"sys_service_provider_id\":\"2088511833207856\""  +
                "    }" +
                "  }" ); //填充业务参数

        /*subject：必填，商品的标题/交易标题/订单标题/订单关键字等。 不可使用特殊字符，如 /,=,& 等。

        product_code：必填，销售产品码，与支付宝签约的产品码名称。目前电脑支付场景下仅支持 FAST_INSTANT_TRADE_PAY。

        total_amount：必填，订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。金额不能为0。*/
        String body=null;
        try {
            body = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return body;
    }

}
