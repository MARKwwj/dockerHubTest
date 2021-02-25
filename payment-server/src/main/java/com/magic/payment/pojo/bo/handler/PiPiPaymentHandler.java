package com.magic.payment.pojo.bo.handler;

import cn.hutool.json.JSONObject;
import com.magic.framework.utils.MagicHttpUtil;
import com.magic.payment.cmmon.util.PayUtil;
import com.magic.payment.pojo.bo.GeneralPaymentHandler;
import com.magic.payment.pojo.bo.NotifyParams;
import com.magic.payment.pojo.dto.CreateOrderDto;

import java.util.HashMap;
import java.util.Map;

public class PiPiPaymentHandler extends GeneralPaymentHandler {
    /**
     * 【目前支持的支付渠道】
     * 支付类型中文名称	渠道编号
     * 微信扫码	        6
     * 微信H5	        7
     * 支付宝H5	        8
     * 支付宝扫码	        9
     *
     * 【返回参数】
     * 参数	说明
     * code	状态码
     * data	返回数据集
     * msg	状态消息
     * {
     *     "code": 1001,
     *     "data": "数据集",
     *     "msg": "成功"
     * }

     */

    @Override
    public String generatePayHtml(CreateOrderDto createOrderDto) {
        Map<String, String> paramMap = new HashMap<>(20);
        paramMap.put("partner_id", createOrderDto.getMerchantCode());
        paramMap.put("channel_id", createOrderDto.getChannelCode());
        paramMap.put("partner_order", createOrderDto.getOrderNumber());
        paramMap.put("pay_method", "支付");
        paramMap.put("user_id", "user_id");
        paramMap.put("total_fee",String.valueOf(Integer.parseInt(createOrderDto.getCommodityInFactAmount())*100));
        paramMap.put("subject", "商品");
        paramMap.put("back_url", "http://www.baidu.com");
        paramMap.put("notify_url", createOrderDto.getNotifyUrl());
        paramMap.put("payextra_param", "payextra_param");
        paramMap.put("exter_invoke_ip", "127.0.0.1");
        paramMap.put("sign_type", "MD5");
        paramMap.put("sign", generateSign(paramMap,createOrderDto.getMerchantPrivateKey()));
        String result = MagicHttpUtil.postStringMap(createOrderDto.getPaymentApiUrl(), paramMap, 10000);
        return checkThirdPartyOrderCreateResult(result);
    }

    @Override
    public NotifyParams convertToNotifyParams(JSONObject notifyParam) {
        return NotifyParams.builder()
                .merchantCode(notifyParam.getStr("partner_id"))
                .orderNumber(notifyParam.getStr("partner_order"))
                .thirdPartyOrderNumber(notifyParam.getStr("order_id"))
                .successTime(notifyParam.getStr("success_time"))
                .orderStatus(notifyParam.getStr("code"))
                .paySuccessStatus("1001")
                .notifyContent("success")
                .rechargeAmount(String.valueOf(Integer.parseInt(notifyParam.getStr("real_money")) / 100))
                .build();
    }

    @Override
    public boolean checkNotifySign(JSONObject paramMap, String key) {
        String sign = PayUtil.createSignByCustomOrder(key, paramMap,
                "partner_id",
                "channel_id"
                , "partner_order"
                , "order_id"
                , "user_id"
                , "total_fee"
                , "success_time"
                , "code");
        return sign.equals(paramMap.get("sign"));
    }


    @Override
    protected String generateSign(Map<String, String> paramMap,String key) {
        return PayUtil.createSignByLexicographicalOrder(key,paramMap,"subject");
    }

}
