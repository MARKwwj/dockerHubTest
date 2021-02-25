package com.magic.longvideo.controller.user;


import com.magic.framework.structs.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@Api(tags = "[视频分享推广文案]")
@RequestMapping("/long_video/user/share")
public class UserShareController {

    @ApiOperation("推广文案")
    @PostMapping("/video_share_content")
    public JsonResult videoShareContent(@RequestBody Map<String, Integer> videoId) {
        //TODO Don 推广
        return JsonResult.successMap("content", "视频分享推广文案" + videoId.get("videoId"));
    }

}
