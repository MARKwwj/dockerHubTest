package com.magic.payment.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.utils.RabbitmqUtil;
import com.magic.payment.cmmon.consts.AppConfigConsts;
import com.magic.payment.cmmon.consts.RedisConsts;
import com.magic.payment.mapper.LongVideoPaymentMerchantConfigMapper;
import com.magic.payment.mapper.LongVideoPaymentOrderRecordMapper;
import com.magic.payment.mapper.LongVideoUserMapper;
import com.magic.payment.pojo.bo.GeneralPaymentHandler;
import com.magic.payment.pojo.bo.NotifyParams;
import com.magic.payment.pojo.bo.PaymentHandlerSelector;
import com.magic.payment.pojo.entity.LongVideoPaymentMerchantConfigEntity;
import com.magic.payment.pojo.entity.LongVideoPaymentOrderRecordEntity;
import com.magic.payment.pojo.entity.LongVideoUserEntity;
import com.magic.payment.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private LongVideoPaymentMerchantConfigMapper longVideoPaymentMerchantConfigMapper;

    @Autowired
    private LongVideoPaymentOrderRecordMapper longVideoPaymentOrderRecordMapper;

    @Autowired
    private LongVideoUserMapper longVideoUserMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String handle(JSONObject body, Integer merchantId) {
        QueryWrapper<LongVideoPaymentMerchantConfigEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchant_id", merchantId);
        LongVideoPaymentMerchantConfigEntity longVideoPaymentMerchantConfigEntity = longVideoPaymentMerchantConfigMapper.selectOne(queryWrapper);
        GeneralPaymentHandler handler = PaymentHandlerSelector.select(longVideoPaymentMerchantConfigEntity.getMchRemark());
        NotifyParams notifyParams = handler.convertToNotifyParams(body);
        String orderNumber=notifyParams.getOrderNumber();
        String paidOrderRedisKey=createPaidKey(orderNumber);
        //因为支付完成，第三方支付平台会进行多次异步回调。需要进行防重复回调的不必要执行
        if(JedisUtil.exist(paidOrderRedisKey)){
            //存在key则说明订单已成功支付，直接返回
            return notifyParams.getNotifyContent();
        }

        //拿到回调结果，转换成我方数据格式后进行 签名和订单状态的校验
        if (!notifyParams.getOrderStatus().equals(notifyParams.getPaySuccessStatus())) {
            return checkMsg("订单支付状态不匹配",notifyParams.getFailContent());
        }
        boolean verify = handler.checkNotifySign(body, longVideoPaymentMerchantConfigEntity.getMerchantPrivateKey());
        if (!verify) {
            return checkMsg("签名校验不通过",notifyParams.getFailContent());
        }

        //更新订单交易状态
        Double rechargeAmount=Double.parseDouble(notifyParams.getRechargeAmount()==null?"0":notifyParams.getRechargeAmount());
        //更新订单状态，交易完成时间以及交易金额等信息
        UpdateWrapper<LongVideoPaymentOrderRecordEntity> OrderRecordUpdateWrapper = new UpdateWrapper<>();
        OrderRecordUpdateWrapper.eq("order_number",orderNumber)
                .ne("order_status", 1)
                .set("order_status", 1)
                .set("success_time",notifyParams.getSuccessTime())
                .set("recharge_amount",rechargeAmount)
                .set("third_party_order_number",notifyParams.getThirdPartyOrderNumber());
        int rows = longVideoPaymentOrderRecordMapper.update(null, OrderRecordUpdateWrapper);
        if (rows <1) {
            return checkMsg("订单交易失败",notifyParams.getFailContent());
        }
        QueryWrapper<LongVideoPaymentOrderRecordEntity> orderRecordEntityQueryWrapper=new QueryWrapper<>();
        orderRecordEntityQueryWrapper.eq("order_number",orderNumber);
        LongVideoPaymentOrderRecordEntity orderRecordEntity=longVideoPaymentOrderRecordMapper.selectOne(orderRecordEntityQueryWrapper);
        Long userId=orderRecordEntity.getUserId();

        //用户上分
        UpdateWrapper<LongVideoUserEntity> userEntityUpdateWrapper=new UpdateWrapper<>();
        userEntityUpdateWrapper.eq("user_id",userId)
                .set("vip_end_time",orderRecordEntity.getVipEndTime())
                .set("gold_coins",orderRecordEntity.getCoinBalance())
                .setSql("total_recharge=total_recharge+"+(int)Math.floor(orderRecordEntity.getRechargeAmount()==null?0:orderRecordEntity.getRechargeAmount()));
        longVideoUserMapper.update(null,userEntityUpdateWrapper);
        //判断是否为官方用户
        LongVideoUserEntity userEntity= Optional.ofNullable(longVideoUserMapper.selectById(userId)).orElseThrow(()->new RuntimeException("用户异常"));
        if(null!=userEntity.getAgentId()&&0!=userEntity.getAgentId()){
            //调用代理分成扣量接口
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("userId",userId);
            paramMap.put("userType",userEntity.getUserType());
            paramMap.put("agentId",userEntity.getAgentId());
            paramMap.put("appId",AppConfigConsts.APP_ID);
            paramMap.put("payOrderId",orderRecordEntity.getThirdPartyOrderNumber());
            paramMap.put("mchOrderId",orderNumber);
            paramMap.put("orderAmount",notifyParams.getRechargeAmount());
            paramMap.put("equipmentType",userEntity.getEquipmentType());
            //orderRecordEntity.getCreateTime()
            paramMap.put("createTime", orderRecordEntity.getCreateTime());
            RabbitmqUtil.sendObject("agentsAmountShare", paramMap);
        }
        //订单交易成功，往redis存一个标识key,value给""即可。
        //只需要判断key是否存在来验证订单是否成功，这里key过期时间设置不宜过长,根据实际情况调整
        JedisUtil.setStr(paidOrderRedisKey,"",RedisConsts.PAID_SUCCESS_ORDER_KEY_EXPIRE_SECONDS);
        return checkMsg("订单交易完成",notifyParams.getNotifyContent());
    }



    private String createPaidKey(String orderNumber){
        return RedisConsts.PAID_SUCCESS_ORDER_KEY+orderNumber;
    }

    /**
     * 响应消息处理
     * @param customMsg 自定义响应提示
     * @param needMsg 第三方规范的响应提示
     * @return {@link java.lang.String} 最终返回提示信息
     */
    private String checkMsg(String customMsg,String needMsg){
        return needMsg==null?customMsg:needMsg;
    }
}
