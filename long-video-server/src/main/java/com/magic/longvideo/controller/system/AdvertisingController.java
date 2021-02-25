package com.magic.longvideo.controller.system;

import com.magic.framework.structs.JsonResult;
import com.magic.longvideo.service.system.AdvertisingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/long_video/advertising")
public class AdvertisingController {

    private final AdvertisingService advertisingService;

    public AdvertisingController(AdvertisingService advertisingService) {
        this.advertisingService = advertisingService;
    }


    @PostMapping
    public JsonResult advertisingList() {
        return JsonResult.success(advertisingService.advertisingList());
    }

    @PostMapping("/specify")
    public JsonResult specify(@RequestBody Map<String, String> adConfigKey) {
        return JsonResult.successMap("adList", advertisingService.specify(adConfigKey.get("adConfigKey")));
    }
}
