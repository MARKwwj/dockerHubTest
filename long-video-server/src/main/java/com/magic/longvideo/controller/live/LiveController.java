package com.magic.longvideo.controller.live;

import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.pojo.vo.LiveListVo;
import com.magic.longvideo.service.live.LiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/long_video/live")
@Api(tags = "[直播列表]")
public class LiveController {

    private final LiveService liveService;

    public LiveController(LiveService liveService) {
        this.liveService = liveService;
    }

    @PostMapping("/list")
    @ApiOperation("直播列表")
    public JsonResult liveList(@RequestBody LiveListVo liveListVo) {
        return JsonResult.success(liveService.liveList(liveListVo));
    }

    @PostMapping("/watched")
    @ApiOperation("直播已观看时长")
    public JsonResult watched(@RequestBody Map<String, Integer> time) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.successMap("watched", liveService.watched(time.get("time"), userId));
    }

}
