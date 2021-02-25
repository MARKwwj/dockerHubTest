package com.magic.sms.service.impl;

import cn.hutool.core.util.PhoneUtil;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.sms.handler.SmsHandler;
import com.magic.sms.mapper.*;
import com.magic.sms.pojo.bean.SmsResultBean;
import com.magic.sms.pojo.bean.SmsSendBean;
import com.magic.sms.pojo.entity.*;
import com.magic.sms.pojo.vo.SendCodeVo;
import com.magic.sms.selector.SmsSelector;
import com.magic.sms.service.SmsService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmsServiceImpl implements SmsService {

    private final LongVideoSmsPassageMapper longVideoSmsPassageMapper;
    private final LongVideoSmsRecordMapper longVideoSmsRecordMapper;
    private final LongVideoSmsMerchantInfoMapper longVideoSmsMerchantInfoMapper;
    private final LongVideoSmsTemplateMapper longVideoSmsTemplateMapper;
    private final LongVideoSmsConfigMapper longVideoSmsConfigMapper;

    public SmsServiceImpl(LongVideoSmsPassageMapper longVideoSmsPassageMapper, LongVideoSmsRecordMapper longVideoSmsRecordMapper, LongVideoSmsMerchantInfoMapper longVideoSmsMerchantInfoMapper, LongVideoSmsTemplateMapper longVideoSmsTemplateMapper, LongVideoSmsConfigMapper longVideoSmsConfigMapper) {
        this.longVideoSmsPassageMapper = longVideoSmsPassageMapper;
        this.longVideoSmsRecordMapper = longVideoSmsRecordMapper;
        this.longVideoSmsMerchantInfoMapper = longVideoSmsMerchantInfoMapper;
        this.longVideoSmsTemplateMapper = longVideoSmsTemplateMapper;
        this.longVideoSmsConfigMapper = longVideoSmsConfigMapper;
    }

    @Override
    public boolean send(SendCodeVo sendCodeVo) {
        //TODO 参数校验
        if (!PhoneUtil.isMobile(sendCodeVo.getPhoneNumber())) {
            return false;
        }
        //TODO 接口防刷，防止有人恶意调用接口

        //TODO 短信单位时间内限量，限制指定时间段内短信最多发送条数

        //TODO 查询短信商户信息
        LongVideoSmsConfigEntity longVideoSmsConfigEntity = longVideoSmsConfigMapper.selectById(sendCodeVo.getAppId());
        LongVideoSmsPassageEntity longVideoSmsPassageEntity = longVideoSmsPassageMapper.selectById(longVideoSmsConfigEntity.getPassageId());
        LongVideoSmsMerchantInfoEntity longVideoSmsMerchantInfoEntity = longVideoSmsMerchantInfoMapper.selectById(longVideoSmsConfigEntity.getSmsMerchantId());
        LongVideoSmsTemplateEntity longVideoSmsTemplateEntity = longVideoSmsTemplateMapper.selectById(longVideoSmsConfigEntity.getTemplateId());
        //TODO 封装发送短信参数
        SmsSendBean smsSendBean = new SmsSendBean();
        MagicBeanUtil.copyProperties(sendCodeVo, smsSendBean);
        MagicBeanUtil.copyProperties(longVideoSmsMerchantInfoEntity, smsSendBean);
        MagicBeanUtil.copyProperties(longVideoSmsTemplateEntity, smsSendBean);
        //TODO 发送短信实现
        SmsHandler handler = SmsSelector.select(longVideoSmsPassageEntity.getPassageRemark());
        String result = handler.sendSms(smsSendBean);
        //TODO 处理发送结果
        SmsResultBean smsResultBean = handler.checkResult(result);
        //TODO 短信记录落地
        longVideoSmsRecordMapper.insert(LongVideoSmsRecordEntity.builder()
                .createTime(new Date())
                .phoneNumber(sendCodeVo.getPhoneNumber())
                .templateParam(sendCodeVo.getTemplateParam())
                .templateId(longVideoSmsTemplateEntity.getTemplateId())
                .sendCode(smsResultBean.getSendStatus())
                .sendResult(smsResultBean.getSendResult())
                .userId(sendCodeVo.getUserId())
                .build());
        return smsResultBean.isSuccess();
    }

}
