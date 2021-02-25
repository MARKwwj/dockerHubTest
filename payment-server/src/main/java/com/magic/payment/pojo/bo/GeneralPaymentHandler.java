package com.magic.payment.pojo.bo;

import cn.hutool.json.JSONObject;
import com.magic.payment.cmmon.consts.PayServiceErrorCode;
import com.magic.payment.pojo.bo.NotifyParams;
import com.magic.payment.pojo.dto.CreateOrderDto;
import lombok.Data;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public abstract class GeneralPaymentHandler {
    private static final Pattern P = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * 校验第三方平台订单创建结果
     * @param resultStr 第三方支付接口响应结果
     * @return String 如果成功返回预支付html页面或跳转脚本，否则返回第三方支付异常的状态码
     */
    protected String checkThirdPartyOrderCreateResult(String resultStr){
        resultStr=resultStr.trim().replace("\r","").replace("\n","");
        if(resultStr.startsWith("<")){
            return resultStr;
        }else{
            return PayServiceErrorCode.THIRD_PARTY_ERROR;
        }
    }

    /**
     * 获取三方支付平台的支付地址，并转换成可以供客户端自动跳转的HTML页面
     * 建议使用 window.location.href 进行自动跳转
     *
     * @param createOrderDto 创建订单所需参数
     * @return 订单html页面
     */
    public abstract String generatePayHtml(CreateOrderDto createOrderDto);

    /**
     * 通过参数创建一个签名，具体实现逻辑查看三方平台提供的开发文档
     *
     * @param paramMap
     * @return 签名
     */
    protected abstract String generateSign(Map<String, String> paramMap, String key);


    /**
     * 将平台回调的参数转换成NotifyParams做通用逻辑处理
     * @param notifyParam
     * @return
     */
    public abstract NotifyParams convertToNotifyParams(JSONObject notifyParam);

    /**
     * 用于校验三方平台回调后的签名，具体逻辑查看三方文档
     *
     * @param paramMap
     * @return 校验结果
     */
    public abstract boolean checkNotifySign(JSONObject paramMap, String key);

}
