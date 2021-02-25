package com.magic.longvideo.controller.user;

import cn.hutool.json.JSONObject;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.pojo.vo.RetrieveAccountByPhoneNumberVo;
import com.magic.longvideo.pojo.vo.RetrieveAccountByQrCodeVo;
import com.magic.longvideo.service.user.UserSafeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/long_video/user/safe")
@Api(tags = "[通过手机号找回，通过二维码找回，二维码信息]")
public class UserSafeController {

    private final UserSafeService userSafeService;

    public UserSafeController(UserSafeService userSafeService) {
        this.userSafeService = userSafeService;
    }

    @PostMapping("/retrieve_by_phone_number")
    @ApiOperation("通过手机号找回")
    public JsonResult retrieveAccountByPhoneNumber(@RequestBody RetrieveAccountByPhoneNumberVo retrieveAccountByPhoneNumberVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        JSONObject jsonObject = userSafeService.retrieveAccountByPhoneNumber(retrieveAccountByPhoneNumberVo, userId);
        String message = (String) jsonObject.remove("message");
        return JsonResult.success(message, jsonObject);
    }

    @PostMapping("/retrieve_by_qr_code")
    @ApiOperation("通过二维码找回")
    public JsonResult retrieveAccountByQrCode(@RequestBody RetrieveAccountByQrCodeVo retrieveAccountByQrCodeVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        if (userSafeService.retrieveAccountByQrCode(retrieveAccountByQrCodeVo, userId)) {
            return JsonResult.success("帐号找回成功", new JSONObject().set("isSuccess", true));
        }
        return JsonResult.success("二维码信息错误", new JSONObject().set("isSuccess", false));
    }


}
