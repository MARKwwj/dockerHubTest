package com.magic.sms.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.magic.sms.pojo.bean.SmsResultBean;
import com.magic.sms.pojo.bean.SmsSendBean;

public class AliYunHandler implements SmsHandler {

    @Override
    public String sendSms(SmsSendBean params) {
        String phoneNumber = params.getPhoneNumber();
        String signName = params.getSignName();
        String templateCode = params.getChannelTemplateNo();
        String templateParam = params.getTemplateParam();
        String accessKeyId = params.getPrivateKey();
        String accessSecret = params.getPrivateKeyPassword();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", new JSONObject().set("code",templateParam).toString());
        try {
            CommonResponse response = client.getCommonResponse(request);
            return response.getData();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String ALI_YUN_SEND_RESULT = "OK";

    @Override
    public SmsResultBean checkResult(String sendResult) {
        JSONObject result = JSONUtil.parseObj(sendResult);
        String code = result.getStr("Code");
        SmsResultBean smsResultBean = SmsResultBean
                .builder()
                .sendResult(result.getStr("Message"))
                .sendStatus(code)
                .build();
        if (ALI_YUN_SEND_RESULT.equals(code)) {
            smsResultBean.setSuccess(true);
        }
        return smsResultBean;
    }
}
