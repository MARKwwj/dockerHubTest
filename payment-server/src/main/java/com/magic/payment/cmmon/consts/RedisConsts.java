package com.magic.payment.cmmon.consts;

public class RedisConsts {
    /**
     * 支付成功缓存的rediskey,防止第三方平台的多次异步回调导致的重复校验和数据处理
     */
    public static final String PAID_SUCCESS_ORDER_KEY="long_video_success_order_";

    /**
     * 支付成功缓存的rediskey 的过期时间。根据实际情况调整(这里暂时默认设置10分钟)。不宜过大
     */
    public static final int PAID_SUCCESS_ORDER_KEY_EXPIRE_SECONDS=10*60;
}
