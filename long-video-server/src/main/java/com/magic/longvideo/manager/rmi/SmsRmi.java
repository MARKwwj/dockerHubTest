package com.magic.longvideo.manager.rmi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("sms-server")
public interface SmsRmi {

    @PostMapping("/sms_server/sms/send")
    boolean sendMessage(@RequestBody Map<String, String> param);
}
