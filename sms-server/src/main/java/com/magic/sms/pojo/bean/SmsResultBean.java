package com.magic.sms.pojo.bean;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class SmsResultBean {

    private String sendStatus;
    private String sendResult;
    private boolean isSuccess;
}
