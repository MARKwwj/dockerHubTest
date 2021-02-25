package com.magic.video.controller;

import com.magic.framework.structs.JsonResult;
import com.magic.video.service.InitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/res_video/init")
@Api(tags = "[获取资源服务器域名]")
public class InitController {

    public final InitService initService;

    public InitController(InitService initService) {
        this.initService = initService;
    }

    @PostMapping("/machine_domain")
    @ApiOperation("获取资源服务器域名")
    public JsonResult machineDomain(){
        return JsonResult.success(initService.machineDomain());
    }

}
