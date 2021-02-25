package com.magic.longvideo.controller.user;

import cn.hutool.json.JSONObject;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.pojo.vo.BindPhoneVo;
import com.magic.longvideo.service.user.UserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/long_video/user/bind")
@Api(tags = "[绑定手机号，绑定邀请码]")
public class UserBindController {

    private final UserBindService userBindService;

    public UserBindController(UserBindService userBindService) {
        this.userBindService = userBindService;
    }

    @PostMapping("/code")
    @ApiOperation("绑定邀请码")
    public JsonResult bindCode(@RequestBody Map<String, String> code) {
        //TODO 解析Code转换成代理ID
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        boolean isBindSuccess = userBindService.bindCode(userId, code.get("code"));
        if (isBindSuccess) {
            return JsonResult.success("邀请码绑定成功", new JSONObject().set("isSuccess", true));
        }
        return JsonResult.success("邀请码不合法!绑定失败", new JSONObject().set("isSuccess", false));
    }

    @PostMapping("/phone")
    @ApiOperation("绑定手机号")
    public JsonResult bindPhone(@RequestBody BindPhoneVo bindPhoneVo) {
        //TODO 绑定手机号
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        boolean isBindSuccess = userBindService.bindPhone(userId, bindPhoneVo.getPhoneNumber(), bindPhoneVo.getVerificationCode());
        if (isBindSuccess) {
            return JsonResult.success("手机号绑定成功", new JSONObject().set("isSuccess", true));
        }
        return JsonResult.success("手机号已绑定或验证码失效", new JSONObject().set("isSuccess", false));
    }

}
