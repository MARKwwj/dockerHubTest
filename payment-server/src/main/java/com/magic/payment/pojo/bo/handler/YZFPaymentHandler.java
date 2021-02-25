package com.magic.payment.pojo.bo.handler;

import cn.hutool.crypto.SecureUtil;
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

public class YZFPaymentHandler extends GeneralPaymentHandler {
    /**
     * 对接相关注意项:
     * 【订单状态（orderStatus）】
     * 订单状态	状态码
     * 待支付	0
     * 支付成功	1
     * 订单退款	9
     *
     * 【支付类型（payType）】
     * 支付类别	payType编码	支付方式
     * H5支付	1	        微信H5
     * H5支付	2	        支付宝H5
     *
     * 【签名规则】
     * 签名加密算法类型：MD5
     * 签名加密过程：
     * 【1】将数据按照Key=Value改为字符串（例：mchId=10000）
     * 【2】将Key=Value以签名顺序以&连接（例：mchId=10000&secret=xxxxx）
     *
     * 【api请求格式要求】
     * 请求方式POST application/json;charset=UTF-8
     */


    @Override
    public String generatePayHtml(CreateOrderDto createOrderDto) {
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("mchId", createOrderDto.getMerchantCode());
        paramMap.put("secret", createOrderDto.getMerchantPrivateKey());
        paramMap.put("signType", "MD5");
        paramMap.put("orderSn", createOrderDto.getOrderNumber());
        paramMap.put("totalFee", String.valueOf(Integer.parseInt(createOrderDto.getCommodityInFactAmount())*100));
        paramMap.put("userId","userId");
        paramMap.put("notifyUrl", createOrderDto.getNotifyUrl());
        paramMap.put("redirectUrl", "http://www.baidu.com");
        paramMap.put("payType", String.valueOf(createOrderDto.getChannelType()));
        paramMap.put("sign", generateSign(paramMap,createOrderDto.getMerchantPrivateKey()));
        String result = HttpUtil.post(createOrderDto.getPaymentApiUrl(),JSONUtil.toJsonStr(paramMap),10000);
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
        return NotifyParams.builder()
                .merchantCode(notifyParam.getStr("mchId"))
                .orderNumber(notifyParam.getStr("orderSn"))
                .thirdPartyOrderNumber(notifyParam.getStr("orderNo"))
                .orderStatus(notifyParam.getStr("status"))
                .rechargeAmount(String.valueOf(Integer.parseInt(notifyParam.getStr("totalFee"))/100))
                .successTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                .paySuccessStatus("1")
                .notifyContent("success")
                .failContent("fail")
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
