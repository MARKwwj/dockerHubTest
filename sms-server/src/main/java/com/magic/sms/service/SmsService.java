package com.magic.sms.service;

import com.magic.sms.pojo.vo.SendCodeVo;

public interface SmsService {
    boolean send(SendCodeVo phoneNumber);
}
