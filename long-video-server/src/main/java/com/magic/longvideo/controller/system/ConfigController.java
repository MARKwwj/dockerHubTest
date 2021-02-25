package com.magic.longvideo.controller.system;

import com.magic.framework.structs.JsonResult;
import com.magic.longvideo.service.system.ConfigService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/long_video/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping
    public JsonResult info(@RequestBody Map<String, List<String>> keys) {
        return JsonResult.success(configService.info(keys.get("keys")));
    }
}
