package com.magic.sms.pojo.bean;

import lombok.Data;

@Data
public class SmsSendBean {

    private String signName;
    private String privateKey;
    private String phoneNumber;
    private String templateParam;
    private String channelTemplateNo;
    private String privateKeyPassword;
}
