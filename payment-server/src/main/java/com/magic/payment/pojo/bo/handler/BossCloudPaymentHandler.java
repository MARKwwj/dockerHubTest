package com.magic.payment.pojo.bo.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magic.framework.utils.MagicHttpUtil;
import com.magic.payment.cmmon.consts.PayServiceErrorCode;
import com.magic.payment.cmmon.util.PayUtil;
import com.magic.payment.pojo.bo.GeneralPaymentHandler;
import com.magic.payment.pojo.bo.NotifyParams;
import com.magic.payment.pojo.dto.CreateOrderDto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Boss云支付处理器
 */
public class BossCloudPaymentHandler extends GeneralPaymentHandler {


    @Override
    public String generatePayHtml(CreateOrderDto createOrderDto) {
        Map<String, String> paramMap = new HashMap<>(12);
        paramMap.put("uid", createOrderDto.getMerchantCode());
        paramMap.put("istype", String.valueOf(createOrderDto.getChannelCode()));
        paramMap.put("notify_url", createOrderDto.getNotifyUrl());
        paramMap.put("return_url", "http://www.baidu.com");
        paramMap.put("orderid", createOrderDto.getOrderNumber());
        paramMap.put("goodsname", "商品");
        paramMap.put("price", String.valueOf(Integer.parseInt(createOrderDto.getCommodityInFactAmount())*100));
        paramMap.put("ip", "127.0.0.1");
        paramMap.put("sign", generateSign(paramMap,createOrderDto.getMerchantPrivateKey()));
        String result = MagicHttpUtil.postStringMap(createOrderDto.getPaymentApiUrl(), paramMap, 10000);
        if(JSONUtil.isJson(result)){
            JSONObject jsonObject = JSONUtil.parseObj(result);
            if(!"SUCCESS".equals(jsonObject.get("return_code"))){
                return PayServiceErrorCode.THIRD_PARTY_ERROR;
            }
        }else if(!result.startsWith("<!DOCTYPE html>")){
            return PayServiceErrorCode.THIRD_PARTY_ERROR;
        }
        return this.checkThirdPartyOrderCreateResult(result);
    }

    @Override
    protected String checkThirdPartyOrderCreateResult(String result){
        if(JSONUtil.isJson(result)){
            JSONObject jsonObject = JSONUtil.parseObj(result);
            if(!"SUCCESS".equals(jsonObject.get("return_code"))){
                return PayServiceErrorCode.THIRD_PARTY_ERROR;
            }
        }else if(!result.startsWith("<!DOCTYPE html>")){
            return PayServiceErrorCode.THIRD_PARTY_ERROR;
        }
        return result;
    }

    @Override
    public NotifyParams convertToNotifyParams(JSONObject notifyParam) {
        JSONObject json=notifyParam.getJSONObject("return_msg");
        return NotifyParams.builder()
                //.merchantCode(notifyParam.get("partner_id"))
                .orderNumber(json.getStr("orderid"))
                .thirdPartyOrderNumber(json.getStr("pay_order_id"))
                .successTime(DateUtil.format(new Date(json.getLong("time_end")*1000),"yyyy-MM-dd HH:mm:ss"))
                .orderStatus(notifyParam.getStr("return_code"))
                .paySuccessStatus("SUCCESS")
                .notifyContent("{\"return_code\":\"OK\"}")
                .rechargeAmount(String.valueOf(json.getInt("price")/100))
                .build();
    }

    @Override
    public boolean checkNotifySign(JSONObject paramMap, String key) {
        String sign = PayUtil.createSignByCustomOrderV2(key,"",paramMap,
                "attach",
                "orderid"
                , "pay_order_id"
                , "time_end"
                , "price");
        return sign.equals(paramMap.get("sign"));
    }

    @Override
    protected String generateSign(Map<String, String> paramMap,String key) {
        return PayUtil.createSignByLexicographicalOrderV2(key,"","",paramMap,"ip");
    }
}
