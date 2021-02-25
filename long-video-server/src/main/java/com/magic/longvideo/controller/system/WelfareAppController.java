package com.magic.longvideo.controller.system;

import com.magic.framework.structs.JsonResult;
import com.magic.longvideo.service.WelfareAppService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/long_video/welfare")
public class WelfareAppController {

    private final WelfareAppService welfareAppService;

    public WelfareAppController(WelfareAppService welfareAppService) {
        this.welfareAppService = welfareAppService;
    }


    @PostMapping
    public JsonResult welfare() {
        return JsonResult.successMap("items", welfareAppService.welfareAppList());
    }
}
