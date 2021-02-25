package com.magic.payment.pojo.bo.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import com.magic.framework.utils.MagicHttpUtil;
import com.magic.payment.cmmon.util.PayUtil;
import com.magic.payment.pojo.bo.GeneralPaymentHandler;
import com.magic.payment.pojo.bo.NotifyParams;
import com.magic.payment.pojo.dto.CreateOrderDto;
import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 要支付(YaoPay)第三方支付对接处理器
 */
public class YaoPaymentHandler extends GeneralPaymentHandler {
    @Override
    public String generatePayHtml(CreateOrderDto createOrderDto) {
        Map<String, String> paramMap = new LinkedHashMap<>(12);
        paramMap.put("mch_id", createOrderDto.getMerchantCode());
        paramMap.put("order_type", String.valueOf(createOrderDto.getChannelType()));
        paramMap.put("out_trade_no", createOrderDto.getOrderNumber());
        paramMap.put("total_fee", String.valueOf(Integer.parseInt(createOrderDto.getCommodityInFactAmount())));
        paramMap.put("body", "body");
        paramMap.put("return_url", "http://www.baidu.com");
        paramMap.put("notify_url", createOrderDto.getNotifyUrl());
        paramMap.put("sign", generateSign(paramMap,createOrderDto.getMerchantPrivateKey()));
        String result = MagicHttpUtil.postStringMap(createOrderDto.getPaymentApiUrl(), paramMap, 10000);
        return checkThirdPartyOrderCreateResult(result);
    }

    @Override
    protected String generateSign(Map<String, String> paramMap, String key) {
        return md5Sign(key,paramMap,"body","return_url","notify_url");
    }

    public String md5Sign(String key,Map<String, String> paramMap, String... ignoreParams) {
        paramMap=new LinkedHashMap<>(paramMap);
        StringBuilder signBuilder = new StringBuilder();
        if (MapUtil.isNotEmpty(paramMap)) {
            if (ignoreParams != null && ignoreParams.length > 0) {
                for (String ignoreParam : ignoreParams) {
                    paramMap.remove(ignoreParam);
                }
            }
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (StrUtil.isNotBlank(entry.getValue())) {
                    signBuilder.append(entry.getValue());
                    signBuilder.append("|");
                }
            }
            if (StrUtil.isNotBlank(key)) {
                signBuilder.append(key);
            } else {
                signBuilder.delete(signBuilder.length() - 1, signBuilder.length());
            }
        }
        return SecureUtil.md5(signBuilder.toString());
    }

    @Override
    public NotifyParams convertToNotifyParams(JSONObject notifyParam) {
        return NotifyParams.builder()
                .merchantCode(notifyParam.getStr("mch_id"))
                .orderNumber(notifyParam.getStr("out_trade_no"))
                .thirdPartyOrderNumber(notifyParam.getStr("orderSn"))
                .orderStatus(notifyParam.getStr("status"))
                .paySuccessStatus("1")
                .notifyContent(notifyParam.getStr("msg"))
                .successTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                .rechargeAmount(String.valueOf(Integer.parseInt(notifyParam.getStr("price"))/100))
                .build();
    }

    @Override
    public boolean checkNotifySign(JSONObject paramMap, String key) {
        String sign = PayUtil.createSignByCustomOrder(key, paramMap,
                "mch_id",
                "order_type"
                , "out_trade_no"
                , "orderid","total_fee");
        return sign.equals(paramMap.get("sign"));
    }
}
