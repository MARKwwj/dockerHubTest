package com.magic.sms.pojo.vo;

import lombok.Data;

@Data
public class SendCodeVo {
    private String phoneNumber;
    private String templateParam;
    private String appId;
    private Long userId;
}
