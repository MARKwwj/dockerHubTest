package com.magic.config.controller;

import com.magic.framework.redis.JedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config_server/config")
public class ConfigController {

    @GetMapping("/ver.json")
    public byte[] verJson() {
        return JedisUtil.get("VER_JSON".getBytes());
    }

    @GetMapping("/{version}/app.json")
    public byte[] appJson(@PathVariable Integer version) {
        return JedisUtil.get("APP_JSON".getBytes());
    }

    @GetMapping("/{version}/api.json")
    public byte[] apiJson(@PathVariable Integer version) {
        return JedisUtil.get("API_JSON".getBytes());
    }

    @GetMapping("/{version}/zip.zip")
    public byte[] zip(@PathVariable Integer version) {
        return JedisUtil.get("ZIP".getBytes());
    }


}
