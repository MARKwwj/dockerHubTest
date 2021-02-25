package com.magic.longvideo.controller.sms;

import cn.hutool.json.JSONObject;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.pojo.vo.SendCodeVo;
import com.magic.longvideo.service.sms.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/long_video/sms")
@Api(tags = "[发送验证码]")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send_code")
    @ApiOperation("发送验证码")
    public JsonResult sendCode(@RequestBody SendCodeVo sendCodeVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        if (smsService.sendCode(sendCodeVo, userId)) {
            return JsonResult.success("验证码发送成功", new JSONObject().set("isSuccess", true));
        }
        return JsonResult.success("验证码发送失败", new JSONObject().set("isSuccess", false));
    }

}
