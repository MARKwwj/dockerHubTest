package com.magic.payment.pojo.bo.handler;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magic.payment.cmmon.consts.PayServiceErrorCode;
import com.magic.payment.cmmon.util.PayUtil;
import com.magic.payment.pojo.bo.GeneralPaymentHandler;
import com.magic.payment.pojo.bo.NotifyParams;
import com.magic.payment.pojo.dto.CreateOrderDto;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 万通支付第三方支付对接处理器
 */
public class WantongPaymentHandler extends GeneralPaymentHandler {
    @Override
    public String generatePayHtml(CreateOrderDto createOrderDto) {
        //此处入参顺序不能改变，否则会导致签名失败
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("mchId", createOrderDto.getMerchantCode());
        paramMap.put("secret", createOrderDto.getMerchantPrivateKey());
        paramMap.put("signType", "MD5");
        paramMap.put("orderSn", createOrderDto.getOrderNumber());
        paramMap.put("totalFee", String.valueOf(Integer.parseInt(createOrderDto.getCommodityInFactAmount())*100));
        paramMap.put("userId", "userId");
        paramMap.put("notifyUrl", createOrderDto.getNotifyUrl());
        paramMap.put("redirectUrl","http://www.baidu.com");
        paramMap.put("payType", String.valueOf(createOrderDto.getChannelType()));
        paramMap.put("sign", generateSign(paramMap,null));
        //该api请求类型为application/json;utf-8
        String result = HttpUtil.post(createOrderDto.getPaymentApiUrl(),JSONUtil.toJsonStr(paramMap),10000);
        //String result = MagicHttpUtil.postStringMap(createOrderDto.getPaymentApiUrl(), paramMap, 10000);
        return checkThirdPartyOrderCreateResult(result,createOrderDto.getMerchantPrivateKey());
    }


    protected String checkThirdPartyOrderCreateResult(String result,String secret){
        if(!JSONUtil.isJson(result)){
            return PayServiceErrorCode.THIRD_PARTY_ERROR;
        }else{
            JSONObject jsonObject = JSONUtil.parseObj(result);
            if(!"200".equals(jsonObject.getStr("code"))){
                return PayServiceErrorCode.THIRD_PARTY_ERROR;
            }
            jsonObject=jsonObject.getJSONObject("data");
            String payUrl=jsonObject.getStr("payUrl");
            jsonObject.set("secret",secret);
            jsonObject.remove("payUrl");
            String finalPayUrl=payUrl+"?"+PayUtil.splicePayUrlParams(jsonObject);
            result = "<script>window.location.href=\"" + finalPayUrl + "\"</script>";
        }
        return result;
    }


    @Override
    protected String generateSign(Map<String, String> paramMap, String key) {
        return PayUtil.createSignByParamMapOrder(null,paramMap,"userId","signType");
    }

    @Override
    public NotifyParams convertToNotifyParams(JSONObject notifyParam) {
        JSONObject json=notifyParam.getJSONObject("data");
        return NotifyParams.builder()
                .merchantCode(json.getStr("mchId"))
                .orderNumber(json.getStr("orderNo"))
                .thirdPartyOrderNumber(json.getStr("orderSn"))
                .orderStatus(json.getStr("status"))
                .paySuccessStatus("1")
                .notifyContent("success")
                .successTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                .rechargeAmount(String.valueOf(Integer.parseInt(json.getStr("price"))/100))
                .build();
    }

    @Override
    public boolean checkNotifySign(JSONObject paramMap, String key) {
        String sign = PayUtil.createSignByCustomOrder(null, paramMap,
                "mchId",
                "totalFee"
                , "orderSn"
                , "status");
        return sign.equals(paramMap.get("sign"));
    }
}
