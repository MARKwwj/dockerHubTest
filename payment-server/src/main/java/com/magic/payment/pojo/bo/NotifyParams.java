package com.magic.payment.pojo.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@Builder
public class NotifyParams {
    /**
     * 三方支付平台提供的商户号
     */
    private final String merchantCode;
    /**
     * 我方平台生成的订单号
     */
    private final String orderNumber;
    /**
     * 三方生成的订单号，部分平台不提供订单号，则无需保存
     */
    private final String thirdPartyOrderNumber;
    /**
     * 订单状态，根据三方平台文档提供的支付结果，判断是否成功，然后转换成我们的支付结果
     */
    private final String orderStatus;
    /**
     * 三方平台订单支付成功时间，一般三方平台都会返回订单支付成功时间，但是返回格式多样，统一用字符串接受后处理成我们需要的格式
     */
    private final String successTime;
    /**
     * 支付商品金额，部分平台携带两个支付金额参数，实际支付金额和商品金额，以实际金额为准
     * 注：一定要仔细确认三方支付平台返回金额的单位，我方平台单位为元，三方平台金额单位不一致，有元有分
     */
    private final String rechargeAmount;

    /**
     * 以下两个参数是根据支付平台提供的文档中的内容填写，
     * paySuccessStatus：确认三方平台订单支付结果是否成功，如 1是成功，2是失败，则paySuccessStatus值填1
     */
    private final String paySuccessStatus;

    /**
     * 三方平台异步回调，我方处理回调成功后的返回结果，具体查看三方平台提供的文档
     */
    private final String notifyContent;
    /**
     * 三方平台异步回调，我方处理回调失败后的返回结果，具体查看三方平台提供的文档
     */
    private final String failContent;
}
