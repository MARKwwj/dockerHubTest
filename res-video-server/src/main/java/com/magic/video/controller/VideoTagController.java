package com.magic.video.controller;

import com.magic.framework.structs.JsonResult;
import com.magic.video.service.VideoTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "[搜索栏的标签]")
@RequestMapping("/res_video/video/tag")
public class VideoTagController {

    private final VideoTagService videoTagService;

    public VideoTagController(VideoTagService videoTagService) {
        this.videoTagService = videoTagService;
    }

    @PostMapping
    @ApiOperation("获取所有标签")
    public JsonResult getTagAll() {
        return JsonResult.successMap("tags",videoTagService.getTagAll());
    }

}
