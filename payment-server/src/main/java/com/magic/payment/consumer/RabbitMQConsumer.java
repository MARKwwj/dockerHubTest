package com.magic.payment.consumer;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.magic.framework.utils.RabbitmqUtil;
import com.magic.payment.cmmon.consts.EquipmentTypeEnum;
import com.magic.payment.mapper.LongVideoPaymentOrderRecordMapper;
import com.magic.payment.mapper.LongVideoUserMapper;
import com.magic.payment.pojo.entity.LongVideoPaymentOrderRecordEntity;
import com.magic.payment.pojo.entity.LongVideoUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Component
public class RabbitMQConsumer {
    @Autowired
    private LongVideoPaymentOrderRecordMapper longVideoPaymentOrderRecordMapper;

    @Autowired
    private LongVideoUserMapper longVideoUserMapper;


    /**
     * 处理代理分成后的结果，将结果同步到数据库中
     * @param message
     */
    @RabbitListener(queues = "agentProfitResultQue")
    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void agentProfitResult(Map<String, Object> message) {
        String orderNumber=null;

        try {
            orderNumber=message.get("orderNumber")==null?"":message.get("orderNumber").toString();
            int isDeduct= Integer.parseInt(String.valueOf(message.get("isDeduct")));
            //isDeduct为是否扣量标识 1是 0否
            if(isDeduct==1){
                QueryWrapper<LongVideoPaymentOrderRecordEntity> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("order_number",orderNumber);
                LongVideoPaymentOrderRecordEntity recordEntity=longVideoPaymentOrderRecordMapper.selectOne(queryWrapper);
                Long userId=recordEntity.getUserId();
                LongVideoUserEntity userEntity=longVideoUserMapper.selectById(userId);
                Integer equipmentType=userEntity.getEquipmentType();
                JSONObject deDuctAmountData=new JSONObject();
                deDuctAmountData.set("dateTime",System.currentTimeMillis());
                if(EquipmentTypeEnum.ANDROID.code.equals(equipmentType)){
                    deDuctAmountData.set("androidDeductionOrder",recordEntity.getRechargeAmount());
                }else if(EquipmentTypeEnum.IOS.code.equals(equipmentType)){
                    deDuctAmountData.set("iosDeductionOrder",recordEntity.getRechargeAmount());
                }else {
                    deDuctAmountData.set("otherDeductionOder",recordEntity.getRechargeAmount());
                }
                RabbitmqUtil.sendObject("equipmentStatisticsQue",deDuctAmountData);
            }
            //处理分成结果（更新订单  是否分成扣量、是否为分级外用户订单信息）
            UpdateWrapper<LongVideoPaymentOrderRecordEntity> orderRecordUpdateWrapper=new UpdateWrapper<>();
            orderRecordUpdateWrapper.eq("order_number",orderNumber);
            orderRecordUpdateWrapper.set("is_deduct",isDeduct);
            orderRecordUpdateWrapper.set("is_out_grade",message.get("isOutGrade"));
            longVideoPaymentOrderRecordMapper.update(null,orderRecordUpdateWrapper);
        } catch (Exception e) {
           log.error("订单[{}]代理分成结果处理失败",orderNumber,e);
        }
    }
}
