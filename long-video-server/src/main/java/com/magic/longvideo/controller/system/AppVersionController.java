package com.magic.longvideo.controller.system;

import com.magic.framework.structs.JsonResult;
import com.magic.longvideo.service.system.AppVersionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/long_video/app/version")
public class AppVersionController {

    private final AppVersionService appVersionService;

    public AppVersionController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }


    @PostMapping
    public JsonResult version(@RequestBody Map<String, Integer> client) {
        return JsonResult.success(appVersionService.version(client.get("equipmentType")));
    }
}
