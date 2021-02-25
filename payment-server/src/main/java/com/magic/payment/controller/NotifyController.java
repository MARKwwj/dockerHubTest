package com.magic.payment.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magic.payment.service.NotifyService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pay_service/notify")
public class NotifyController {

    private final NotifyService notifyService;

    public NotifyController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @RequestMapping("/{merchantId}")
    public String notify(@PathVariable("merchantId") Integer merchantId, @RequestParam Map<String,Object> query, @RequestBody(required = false)  JSONObject paramMap) {
        if (MapUtil.isNotEmpty(paramMap)) {
            return notifyService.handle(paramMap, merchantId);
        } else if (MapUtil.isNotEmpty(query)) {
            JSONObject json= JSONUtil.parseObj(query);
            return notifyService.handle(json, merchantId);
        }
        return "请求参数不能为空";
    }

}
