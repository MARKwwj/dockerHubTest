package com.magic.video.controller;

import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.video.pojo.dto.NewestVideoVo;
import com.magic.video.pojo.vo.VideoCollectionVo;
import com.magic.video.pojo.vo.VideoFilterParamsVo;
import com.magic.video.pojo.vo.VideoSearchVo;
import com.magic.video.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/res_video/video")
@Api(tags = "[视频详情，最新影片，视频合集，猜你喜欢，视频筛选，分类列表]")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/info")
    @ApiOperation("视频详情")
    public JsonResult info(@RequestBody @ApiParam("视频ID") Map<String, Integer> videoId) {
        return JsonResult.success(videoService.info(videoId.get("videoId")));
    }

    @PostMapping("/collection")
    @ApiOperation("合集视频")
    public JsonResult collection(@RequestBody VideoCollectionVo videoCollectionVo) {
        return JsonResult.success(videoService.collection(videoCollectionVo));
    }

    @PostMapping("/newest")
    @ApiOperation("最新影片")
    public JsonResult newest(@RequestBody NewestVideoVo newestVideoVo) {
        return JsonResult.success(videoService.newest(newestVideoVo));
    }

    @PostMapping("/filter")
    @ApiOperation("视频筛选")
    public JsonResult filter(@RequestBody @Validated VideoFilterParamsVo videoFilterParamsVo) {
        return JsonResult.success(videoService.filter(videoFilterParamsVo));
    }

    @PostMapping("/classify")
    @ApiOperation("根据合集ID获取分类列表")
    public JsonResult classify(@RequestBody Map<String, Integer> categoryId) {
        return JsonResult.successMap("classifyList", videoService.classify(categoryId.get("categoryId")));
    }

    @PostMapping("/recommend")
    @ApiOperation("视频推荐，原理利用搜索引擎搜索视频标签")
    public JsonResult recommend(@RequestBody VideoSearchVo videoSearchVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return videoService.recommend(videoSearchVo, userId);
    }


}
