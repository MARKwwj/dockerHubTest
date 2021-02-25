package com.magic.sms.handler;

import com.magic.sms.pojo.bean.SmsResultBean;
import com.magic.sms.pojo.bean.SmsSendBean;

public interface SmsHandler {

    /**
     * 用于实现短信平台发送短信的业务逻辑
     *
     * @param params 平台发送短信所需的参数，不同平台可能参数不一致，根据具体情况维护SmsSendBean这个对象
     * @return 平台发送短信后返回的结果，不同平台返回的结果可能不一致，所以推荐转换成Json字符串后传入
     * checkResult方法中进行处理
     */
    String sendSms(SmsSendBean params);

    /**
     * 处理短信平台发送短信,将结果转换成SmsResultBean
     *
     * @param sendResult 短信发送结果
     * @return 处理好的结果，统一写入数据库
     */
    SmsResultBean checkResult(String sendResult);

}
