package com.magic.longvideo.controller.user;

import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.service.user.UserDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/long_video/user/daily")
public class UserDailyController {

    @Autowired
    private UserDailyService userDailyService;

    @PostMapping("/download")
    public JsonResult videoDownloadLimit() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.successMap("downloadCount",userDailyService.getTodayDownloadCount(userId));
    }
}
