package com.magic.sms.selector;

import com.magic.sms.handler.AliYunHandler;
import com.magic.sms.handler.SmsHandler;

import java.util.HashMap;

public class SmsSelector {

    private static HashMap<String, SmsHandler> handlerMap = new HashMap<>(3);

    static {
        handlerMap.put("AliYun", new AliYunHandler());
    }

    public static SmsHandler select(String remark) {
        return handlerMap.get(remark);
    }
}
