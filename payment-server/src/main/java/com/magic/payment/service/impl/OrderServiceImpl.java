package com.magic.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.payment.cmmon.consts.*;
import com.magic.payment.cmmon.util.PayUtil;
import com.magic.payment.mapper.*;
import com.magic.payment.pojo.bo.GeneralPaymentHandler;
import com.magic.payment.pojo.bo.PaymentHandlerSelector;
import com.magic.payment.pojo.dto.CreateOrderDto;
import com.magic.payment.pojo.entity.*;
import com.magic.payment.service.OrderService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final LongVideoPaymentChannelMapper longVideoPaymentChannelMapper;

    private final LongVideoPaymentMerchantConfigMapper longVideoPaymentMerchantConfigMapper;

    private final LongVideoPaymentCommodityInfoMapper longVideoPaymentCommodityInfoMapper;

    private final LongVideoPaymentOrderRecordMapper longVideoPaymentOrderRecordMapper;

    private final LongVideoUserMapper longVideoUserMapper;

    public OrderServiceImpl(
            LongVideoPaymentChannelMapper longVideoPaymentChannelMapper,
            LongVideoPaymentMerchantConfigMapper longVideoPaymentMerchantConfigMapper,
            LongVideoPaymentCommodityInfoMapper longVideoPaymentCommodityInfoMapper,
            LongVideoPaymentOrderRecordMapper longVideoPaymentOrderRecordMapper,
            LongVideoUserMapper longVideoUserMapper) {
        this.longVideoPaymentOrderRecordMapper = longVideoPaymentOrderRecordMapper;
        this.longVideoPaymentChannelMapper = longVideoPaymentChannelMapper;
        this.longVideoPaymentMerchantConfigMapper = longVideoPaymentMerchantConfigMapper;
        this.longVideoPaymentCommodityInfoMapper = longVideoPaymentCommodityInfoMapper;
        this.longVideoUserMapper = longVideoUserMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(Long userId, Integer appType, Integer channelId, Integer commodityId) {
        LongVideoPaymentChannelEntity longVideoPaymentChannelEntity = longVideoPaymentChannelMapper.selectById(channelId);
        LongVideoPaymentMerchantConfigEntity longVideoPaymentMerchantConfigEntity = longVideoPaymentMerchantConfigMapper.selectById(longVideoPaymentChannelEntity.getMerchantId());
        LongVideoPaymentCommodityInfoEntity longVideoPaymentCommodityInfoEntity = longVideoPaymentCommodityInfoMapper.selectById(commodityId);
        CreateOrderDto createOrderDto = new CreateOrderDto();
        MagicBeanUtil.copyProperties(longVideoPaymentMerchantConfigEntity,createOrderDto);
        MagicBeanUtil.copyProperties(longVideoPaymentCommodityInfoEntity,createOrderDto);
        MagicBeanUtil.copyProperties(longVideoPaymentChannelEntity,createOrderDto);
        String orderNumber = PayUtil.createOrderNo();
        createOrderDto.setOrderNumber(orderNumber);
        GeneralPaymentHandler handler = PaymentHandlerSelector.select(longVideoPaymentMerchantConfigEntity.getMchRemark());
        String html = handler.generatePayHtml(createOrderDto);

        //创建订单记录
        LongVideoUserEntity userEntity =Optional.ofNullable(longVideoUserMapper.selectById(userId)).orElseThrow(()->new RuntimeException("用户异常"));
        LongVideoPaymentOrderRecordEntity orderRecord=new LongVideoPaymentOrderRecordEntity();
        orderRecord.setUserId(userId);
        orderRecord.setNickName(userEntity.getNickName());
        //当前余额总金币数=用户当前金币余额+赠送金币数
        int currentCoins=userEntity.getGoldCoins()+Optional.ofNullable(longVideoPaymentCommodityInfoEntity.getCommodityGift()).orElse(0);
        int commodityValue=Optional.ofNullable(longVideoPaymentCommodityInfoEntity.getCommodityValue()).orElse(0);
        Date vipEndTime=null;
        if(CommodityTypeConsts.COIN==longVideoPaymentCommodityInfoEntity.getCommodityType()){
            //当前余额总金币数=用户当前金币余额+赠送金币数+商品数值
            currentCoins+=commodityValue;
            vipEndTime=userEntity.getVipEndTime();
        }else if(CommodityTypeConsts.VIP==longVideoPaymentCommodityInfoEntity.getCommodityType()){
            //如果商品为VIP类型，给用户当前VIP到期时间加上 商品数值（秒数)
            vipEndTime=DateUtils.addSeconds(Optional.ofNullable(userEntity.getVipEndTime()).orElse(new Date()),commodityValue);
        }
        orderRecord.setCoinBalance(currentCoins);
        orderRecord.setVipEndTime(vipEndTime);
        orderRecord.setOrderNumber(orderNumber);
        orderRecord.setChannelId(channelId);
        orderRecord.setOrderStatus(OrderStatus.UNPAID);
        orderRecord.setCreateTime(new Date());
        orderRecord.setRechargeMethod(longVideoPaymentChannelEntity.getChannelType());
        orderRecord.setCommodityName(longVideoPaymentCommodityInfoEntity.getCommodityName());
        orderRecord.setCommodityGift(longVideoPaymentCommodityInfoEntity.getCommodityGift());
        orderRecord.setCommodityInFactAmount(longVideoPaymentCommodityInfoEntity.getCommodityInFactAmount());
        orderRecord.setCommodityType(longVideoPaymentCommodityInfoEntity.getCommodityType());
        orderRecord.setCommodityValue(longVideoPaymentCommodityInfoEntity.getCommodityValue());
        int isOfficial=(userEntity.getAgentId()==null||userEntity.getAgentId()==0)?1:0;
        orderRecord.setIsOfficial(isOfficial);
        //orderRecord.setDelFlag(2); 数据库提供了默认值
        Double commodityInFactAmount=Double.valueOf(longVideoPaymentCommodityInfoEntity.getCommodityInFactAmount());
        Double rateAmount=PayUtil.rateAmountCompute(commodityInFactAmount,longVideoPaymentChannelEntity.getRate());
        orderRecord.setRateAmount(rateAmount);
        longVideoPaymentOrderRecordMapper.insert(orderRecord);
        JedisUtil.setStr(orderNumber, html, 60 * 60 * 24 * 15);
        return orderNumber;
    }


    @Override
    public String pay(String orderNumber) {
        String errorMsg;
        String result = JedisUtil.getStr(orderNumber);
        if(null!=result){
            if(result.startsWith("<")){
                //判断redis中第三方订单创建结果是否为html，如果是直接返回（考虑到可能会有<script>/<!DOCTYPE html>等直接判断是否<开头即可）
                return result;
            }else if ((errorMsg = PayServiceExceptionEnum.errorMsg(result)) != null) {
                //否则判断异常状态码是否在第三方api对接异常枚举中，是的话返回状态码对应的异常提示
                return errorMsg;
            }
        }
        //认为订单不存在或已过期
        return Objects.requireNonNull(PayServiceExceptionEnum.errorMsg(PayServiceErrorCode.NO_PAY_URL));
    }

    @Override
    public boolean payResult(String orderNumber) {
        QueryWrapper<LongVideoPaymentOrderRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("order_number", orderNumber)
                .and(condition->condition.eq("order_status", OrderStatus.SUCCESS)
                        .or()
                        .eq("order_status", OrderStatus.REPLENISHMENT)
                );
        int count = longVideoPaymentOrderRecordMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
