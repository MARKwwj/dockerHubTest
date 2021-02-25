package com.magic.video.controller;

import com.magic.framework.redis.JedisUtil;
import com.magic.framework.structs.JsonResult;
import com.magic.video.common.consts.VideoRankDateTypePrefix;
import com.magic.video.common.consts.VideoRankType;
import com.magic.video.pojo.dto.RankListDto;
import com.magic.video.pojo.dto.VideoRankListVo;
import com.magic.video.service.VideoRankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/res_video/video/rank")
@Api(tags = "[视频排行榜]")
public class VideoRankController {

    private final VideoRankService videoRankService;

    public VideoRankController(VideoRankService videoRankService) {
        this.videoRankService = videoRankService;
    }

    @PostMapping("/list")
    @ApiOperation("获取排行榜内容")
    public JsonResult videoRankListByRankId(@RequestBody @Validated VideoRankListVo videoRankListVo) {
        RankListDto rankListDto = videoRankService.getVideoRankListByRankType(videoRankListVo.getRankType(), videoRankListVo.getRankDateType());
        return JsonResult.success(rankListDto);
    }

    @PostMapping("/delete/cache")
    @ApiOperation("删除缓存")
    public JsonResult deleteCache(@RequestBody @Validated VideoRankListVo videoRankListVo) {
        String suffix = VideoRankType.getValueByKey(videoRankListVo.getRankType());
        String prefix = VideoRankDateTypePrefix.getValueByKey(videoRankListVo.getRankDateType());
        String rankListName = prefix + suffix;
        JedisUtil.del(rankListName);
        return JsonResult.success();
    }

}
