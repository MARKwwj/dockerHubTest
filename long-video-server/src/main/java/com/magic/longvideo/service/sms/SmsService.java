package com.magic.longvideo.service.sms;


import com.magic.longvideo.pojo.vo.SendCodeVo;

public interface SmsService {

    boolean sendCode(SendCodeVo phoneNumber, Long userId);
}
