package com.magic.longvideo.service.sms.impl;

import com.magic.framework.utils.MagicUtil;
import com.magic.longvideo.manager.rmi.SmsRmi;
import com.magic.longvideo.pojo.vo.SendCodeVo;
import com.magic.longvideo.service.sms.SmsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {

    private final SmsRmi smsRmi;

    public SmsServiceImpl(SmsRmi smsRmi) {
        this.smsRmi = smsRmi;
    }

    @Override
    public boolean sendCode(SendCodeVo sendCodeVo, Long userId) {
        String code = MagicUtil.generateVerificationCode(sendCodeVo.getPhoneNumber());
        Map<String, String> param = new HashMap<>(4);
        param.put("userId", String.valueOf(userId));
        param.put("templateParam", code);
        param.put("phoneNumber", sendCodeVo.getPhoneNumber());
        param.put("appId", sendCodeVo.getAppId());
        return smsRmi.sendMessage(param);
    }
}
