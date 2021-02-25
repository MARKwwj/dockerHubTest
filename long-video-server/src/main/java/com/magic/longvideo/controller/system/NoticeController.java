package com.magic.longvideo.controller.system;

import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.service.system.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/long_video/notice")
@Api(tags = "[轮播公告，首页弹框，系统通知，阅读系统通知，赠送通知]")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/loop_notice")
    @ApiOperation("轮播公告")
    public JsonResult loopNotice(@RequestBody Map<String, Integer> type) {
        return JsonResult.success(noticeService.loopNotice(type.get("type")));
    }

    @PostMapping("/pop_notice")
    @ApiOperation("首页弹框")
    public JsonResult popNotice() {
        return JsonResult.success(noticeService.popNotice());
    }

    @PostMapping("/system_notice")
    @ApiOperation("系统通知")
    public JsonResult systemNotice() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.success(noticeService.systemNotice(userId));
    }

    @PostMapping("/system_notice/read")
    @ApiOperation("阅读系统通知")
    public JsonResult readSystemNotice(@RequestBody Map<String, Integer> noticeId) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.success(noticeService.readSystemNotice(userId, noticeId.get("noticeId")));
    }

    @PostMapping("/gift_notice")
    @ApiOperation("赠送通知")
    public JsonResult giftNotice() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.successMap("giftNoticeList", noticeService.giftNotice(userId));
    }

}
