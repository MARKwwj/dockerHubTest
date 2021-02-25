package com.magic.payment.pojo.bo;

import com.magic.payment.pojo.bo.handler.*;

import java.util.HashMap;

public class PaymentHandlerSelector {

    private static final HashMap<String, GeneralPaymentHandler> handlerMap = new HashMap<>(10);

    static {
        handlerMap.put("PiPi", new PiPiPaymentHandler());
        handlerMap.put("BOSS", new BossCloudPaymentHandler());
        handlerMap.put("WT", new WantongPaymentHandler());
        handlerMap.put("YP", new YaoPaymentHandler());
        handlerMap.put("YZF", new YZFPaymentHandler());
        handlerMap.put("DL", new DLPaymentHandler());
    }

    public static GeneralPaymentHandler select(String remark) {
        return handlerMap.get(remark);
    }
}
