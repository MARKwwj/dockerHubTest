package com.magic.payment.cmmon.consts;

public enum PayServiceExceptionEnum {

    NO_PASSAGE(PayServiceErrorCode.NO_PASSAGE, "<h2>沒有配置此支付平台,请检查后台配置</h2>"),
    THIRD_PARTY_ERROR(PayServiceErrorCode.THIRD_PARTY_ERROR, "<h2>第三方支付平台异常</h2>"),
    CREATE_ORDER_ERROR(PayServiceErrorCode.CREATE_ORDER_ERROR, "<h2>创建订单异常</h2>"),
    NO_PAY_URL(PayServiceErrorCode.NO_PAY_URL, "<h2>支付页面已过期或订单不存在！请重新下单</h2>"),
    ORDER_CREATE_FREQUENTLY(PayServiceErrorCode.ORDER_CREATE_FREQUENTLY, "<h2>订单创建频繁，请稍后再试</h2>");

    private String errorCode;
    private String errorMsg;

    PayServiceExceptionEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static String errorMsg(String errorCode) {
        PayServiceExceptionEnum[] values = PayServiceExceptionEnum.values();
        for (PayServiceExceptionEnum value : values) {
            if (value.errorCode.equals(errorCode)) {
                return value.errorMsg;
            }
        }
        return null;
    }
}
