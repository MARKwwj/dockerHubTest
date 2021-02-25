package com.magic.longvideo.controller.user;

import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.service.user.UserRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/long_video/user/record")
@Api(tags = "[金币明细，充值记录]")
public class UserRecordController {

    private final UserRecordService userRecordService;

    public UserRecordController(UserRecordService userRecordService) {
        this.userRecordService = userRecordService;
    }

    @PostMapping("/coin_details")
    @ApiOperation("金币明细")
    public JsonResult coinDetails() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.successMap("coinDetails",userRecordService.getCoinDetails(userId));
    }

    @PostMapping("/recharge_record")
    @ApiOperation("充值记录")
    public JsonResult rechargeRecord() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.successMap("rechargeRecord",userRecordService.getRechargeRecord(userId));
    }


}
