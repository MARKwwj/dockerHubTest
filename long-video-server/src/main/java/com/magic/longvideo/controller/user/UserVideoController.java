package com.magic.longvideo.controller.user;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.common.consts.UserVideoMapFieldName;
import com.magic.longvideo.common.consts.VideoRankList;
import com.magic.longvideo.manager.UserManager;
import com.magic.longvideo.pojo.dto.VideoInfoDto;
import com.magic.longvideo.pojo.vo.VideoInfoVo;
import com.magic.longvideo.service.user.UserVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/long_video/user/video")
@Api(tags = "[观看历史，我的收藏，已购买视频]")
public class UserVideoController {

    private final UserVideoService userVideoService;
    private final UserManager userManager;

    public UserVideoController(
            UserManager userManager,
            UserVideoService userVideoService
    ) {
        this.userManager = userManager;
        this.userVideoService = userVideoService;
    }

    @PostMapping("/watch")
    @ApiOperation("观看视频相关操作")
    public JsonResult addHistory(@RequestBody VideoInfoVo videoInfoVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        //添加至观看历史
        userVideoService.addVideoInfoToUserVideoMapByFieldName(userId, videoInfoVo, UserVideoMapFieldName.HISTORY);
        //当前视频观影榜分数加1
        userVideoService.increaseVideoRankListScore(videoInfoVo, VideoRankList.WATCH_RANK_LIST);
        return JsonResult.success();
    }

    /**
     * 获取观看历史数据
     *
     * @return
     */
    @PostMapping("/history")
    @ApiOperation("获取观看历史")
    public JsonResult history() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        List<VideoInfoDto> historyList = userVideoService.getVideoInfoFromUserVideoMapByFieldName(userId, UserVideoMapFieldName.HISTORY);
        Date today = new Date();
        JSONObject jsonObject = new JSONObject();
        for (VideoInfoDto dto : historyList) {
            Date watchDate = dto.getWatchDate();
            if (DateUtil.isSameDay(today, watchDate)) {
                jsonObject.append("今天", dto);
            } else if (DateUtil.isIn(watchDate, DateUtil.offsetDay(today, -7), today)) {
                jsonObject.append("一周内", dto);
            } else {
                jsonObject.append("更早", dto);
            }
        }
        List<JSONObject> result = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            List<VideoInfoDto> value = (List<VideoInfoDto>) entry.getValue();
            if (value != null && value.size() != 0) {
                result.add(new JSONObject().putOnce("name", entry.getKey()).putOnce("list", entry.getValue()));
            }
        }
        return JsonResult.successMap("history", result);
    }


    @PostMapping("/history/remove")
    @ApiOperation("删除观看历史")
    public JsonResult removeHistory(@RequestBody Map<String, List<Long>> videoIds) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        boolean isRemove = userVideoService.removeVideoInfoFromUserVideoMapByFieldName(userId, videoIds.get("videoIds"), UserVideoMapFieldName.HISTORY);
        return JsonResult.success(isRemove);
    }

    /**
     * 获取观看我的收藏
     *
     * @return
     */
    @PostMapping("/favorite")
    @ApiOperation("获取我的收藏")
    public JsonResult favorite() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        List<VideoInfoDto> favoriteList = userVideoService.getVideoInfoFromUserVideoMapByFieldName(userId, UserVideoMapFieldName.FAVORITE);
        return JsonResult.successMap("favoriteList", favoriteList);
    }

    @PostMapping("/favorite/add")
    @ApiOperation("添加我的收藏")
    public JsonResult addFavorite(@RequestBody VideoInfoVo videoInfoVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        boolean isAdd = userVideoService.addVideoInfoToUserVideoMapByFieldName(userId, videoInfoVo, UserVideoMapFieldName.FAVORITE);
        //当前视频收藏榜榜分数加1
        userVideoService.increaseVideoRankListScore(videoInfoVo, VideoRankList.FAVOURITE_RANK_LIST);
        return JsonResult.success(isAdd);
    }

    @PostMapping("/favorite/remove")
    @ApiOperation("删除收藏")
    public JsonResult removeFavorite(@RequestBody Map<String, List<Long>> videoIds) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        boolean isRemove = userVideoService.removeVideoInfoFromUserVideoMapByFieldName(userId, videoIds.get("videoIds"), UserVideoMapFieldName.FAVORITE);
        return JsonResult.success(isRemove);
    }


    /**
     * 获取已购买视频
     *
     * @return
     */
    @PostMapping("/purchased")
    @ApiOperation("获取已购买视频")
    public JsonResult purchased() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        List<VideoInfoDto> favoriteList = userVideoService.getVideoInfoFromUserVideoMapByFieldName(userId, UserVideoMapFieldName.PURCHASED);
        return JsonResult.successMap("purchasedList", favoriteList);
    }

    @PostMapping("/buy")
    @ApiOperation("购买视频")
    public JsonResult buy(@RequestBody VideoInfoVo videoInfoVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        if (!userManager.deductCoins(videoInfoVo.getVideoPayCoin())) {
            return JsonResult.success("购买失败，余额不足");
        }
        if (!userVideoService.addVideoInfoToUserVideoMapByFieldName(userId, videoInfoVo, UserVideoMapFieldName.PURCHASED)) {
            return JsonResult.failed("购买失败，系统错误");
        }
        userVideoService.updateUserCoinDetails(userId, videoInfoVo.getVideoTitle(), videoInfoVo.getVideoPayCoin());
        userVideoService.increaseVideoRankListScore(videoInfoVo, VideoRankList.PAID_RANK_LIST);
        return JsonResult.success("购买成功");
    }


    @PostMapping("/praise")
    @ApiOperation("点赞视频")
    public JsonResult praise(@RequestBody Map<String, Integer> videoId) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.success(userVideoService.praise(videoId.get("videoId"), userId));
    }


    @PostMapping("/download")
    @ApiOperation("下载视频")
    public JsonResult download(@RequestBody VideoInfoVo videoInfoVo) {
        userVideoService.increaseVideoRankListScore(videoInfoVo, VideoRankList.DOWNLOAD_RANK_LIST);
        return JsonResult.success();
    }


//    @PostMapping("/purchased/add")
//    @ApiOperation("购买视频")
//    public JsonResult addPurchased(@RequestBody VideoInfoVo videoInfoVo) {
//        Long userId = TokenUtil.getUserIdByTokenFromHeader();
//        boolean isAdd = userVideoService.addVideoInfoToUserVideoMapByFieldName(userId, videoInfoVo, UserVideoMapFieldName.PURCHASED);
//        return JsonResult.success(isAdd);
//    }
//
//    @PostMapping("/purchased/remove")
//    public JsonResult removePurchased(@RequestBody List<Long> videoId) {
//        Long userId = TokenUtil.getUserIdByTokenFromHeader();
//        boolean isRemove = userVideoService.removeVideoInfoFromUserVideoMapByFieldName(userId, videoId, UserVideoMapFieldName.PURCHASED);
//        return JsonResult.success(isRemove);
//    }

}
