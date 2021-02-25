package com.magic.sms.controller;

import com.magic.sms.pojo.vo.SendCodeVo;
import com.magic.sms.service.SmsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms_server/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public boolean send(@RequestBody SendCodeVo sendCodeVo) {
        return smsService.send(sendCodeVo);
    }

}
