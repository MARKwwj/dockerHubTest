package com.magic.payment.pojo.bo.handler;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magic.payment.cmmon.util.PayUtil;
import com.magic.payment.pojo.bo.GeneralPaymentHandler;
import com.magic.payment.pojo.bo.NotifyParams;
import com.magic.payment.pojo.dto.CreateOrderDto;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 鼎力支付支付对接处理器
 */
public class DLPaymentHandler extends GeneralPaymentHandler {
    @Override
    public String generatePayHtml(CreateOrderDto createOrderDto) {

        Map<String, String> paramMap = new HashMap<>(20);
        paramMap.put("pid", createOrderDto.getMerchantCode());
        paramMap.put("type", String.valueOf(createOrderDto.getChannelCode()));
        paramMap.put("out_trade_no", createOrderDto.getOrderNumber());
        paramMap.put("notify_url", createOrderDto.getNotifyUrl());
        paramMap.put("return_url", "http://www.baidu.com");
        paramMap.put("name", "商品");
        paramMap.put("money", String.valueOf(Integer.parseInt(createOrderDto.getCommodityInFactAmount())));
        paramMap.put("sitename", "sitename");
        paramMap.put("sign_type", "MD5");
        paramMap.put("sign", generateSign(paramMap,createOrderDto.getMerchantPrivateKey()));
        String result = HttpUtil.post(createOrderDto.getPaymentApiUrl(),JSONUtil.toJsonStr(paramMap),10000);
        return this.checkThirdPartyOrderCreateResult(result);
    }

    @Override
    protected String generateSign(Map<String, String> paramMap, String key) {
        return PayUtil.createSignByLexicographicalOrderV2(key,"&","",paramMap,"sign","sign_type");
    }

    @Override
    public NotifyParams convertToNotifyParams(JSONObject notifyParam) {
        return NotifyParams.builder()
                .merchantCode(notifyParam.getStr("pid"))
                .orderNumber(notifyParam.getStr("out_trade_no"))
                .thirdPartyOrderNumber(notifyParam.getStr("trade_no"))
                .orderStatus(notifyParam.getStr("trade_status"))
                //单位元
                .rechargeAmount(String.valueOf(Integer.parseInt(notifyParam.getStr("money"))))
                .successTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                .paySuccessStatus("TRADE_SUCCESS")
                .notifyContent("success")
                .failContent("fail")
                .build();
    }

    @Override
    public boolean checkNotifySign(JSONObject paramMap, String key) {
        Map<String,String> params=new HashMap<>();
        for (Map.Entry<String, Object> entry:paramMap.entrySet()) {
            params.put(entry.getKey(),entry.getValue()!=null?entry.getValue().toString():null);
        }
        String sign =generateSign(params,key);
        return sign.equals(paramMap.get("sign"));
    }
}
