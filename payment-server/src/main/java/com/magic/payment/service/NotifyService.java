package com.magic.payment.service;

import cn.hutool.json.JSONObject;


public interface NotifyService {
    String handle(JSONObject body, Integer merchantId);
}
