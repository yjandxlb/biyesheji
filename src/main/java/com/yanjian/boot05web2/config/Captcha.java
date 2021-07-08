package com.yanjian.boot05web2.config;

import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.springframework.context.annotation.Configuration;
//登录滑动验证
public class Captcha {
    public static String yanzhengma(String ticket,String randStr) {
        try {

            Credential cred = new Credential("AKIDf6PjnaHROGrBSUwwCDxfEm44nUv9CpV1",
                    "d0KAsfLB6k9GOGxNWO3SbS4Wmpb7Znvp");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("captcha.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CaptchaClient client = new CaptchaClient(cred, "", clientProfile);

            DescribeCaptchaResultRequest req = new DescribeCaptchaResultRequest();
            req.setCaptchaType(9L);
            req.setTicket(ticket);
            req.setUserIp("127.0.0.1");
            req.setRandstr(randStr);
            req.setCaptchaAppId(2011903066L);
            req.setAppSecretKey("0WkRntin7MFksZpMAm0yfxQ**");

            DescribeCaptchaResultResponse resp = client.DescribeCaptchaResult(req);
            System.out.println(DescribeCaptchaResultResponse.toJsonString(resp));
            if("ok".equalsIgnoreCase(resp.getCaptchaMsg())){
                return "true";
            }
            else {
                return "false";
            }

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
