package com.yanjian.boot05web2.config;

import cn.hutool.Hutool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;
import org.springframework.context.annotation.Configuration;;import javax.lang.model.element.NestingKind;
import java.util.Random;
//手机短信验证
    public class SendSms
    {
        public  static String sms(String telphone){
            try{

                Credential cred = new Credential("AKIDf6PjnaHROGrBSUwwCDxfEm44nUv9CpV1",
                        "d0KAsfLB6k9GOGxNWO3SbS4Wmpb7Znvp");

                HttpProfile httpProfile = new HttpProfile();
                httpProfile.setEndpoint("sms.tencentcloudapi.com");

                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setHttpProfile(httpProfile);

                SmsClient client = new SmsClient(cred, "ap-shanghai", clientProfile);

                SendSmsRequest req = new SendSmsRequest();
                String[] phoneNumberSet1 = {"+86"+telphone};
                req.setPhoneNumberSet(phoneNumberSet1);
                String i = String.valueOf(RandomUtil.randomInt(1000, 10000));
                String[] templateParamSet1 = {i};
                req.setTemplateParamSet(templateParamSet1);

                req.setTemplateID("928746");
                req.setSign("yjandxlb");
                req.setSmsSdkAppid("1400510821");

                SendSmsResponse resp = client.SendSms(req);
                System.out.println(SendSmsResponse.toJsonString(resp));
                return i;
            } catch (TencentCloudSDKException e) {
                System.out.println(e.toString());
            }
        return "false";

        }

    }

