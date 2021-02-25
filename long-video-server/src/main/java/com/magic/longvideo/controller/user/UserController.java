package com.magic.longvideo.controller.user;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.magic.framework.structs.JsonResult;
import com.magic.longvideo.pojo.dto.UserInfoDto;
import com.magic.longvideo.pojo.vo.UserJoinVo;
import com.magic.longvideo.pojo.vo.UserLoginVo;
import com.magic.longvideo.service.user.LongVideoUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 仁义礼智信
 */
@RestController
@RequestMapping("/long_video/user")
@Api(tags = "[用户登录，注册，用户信息]")
public class UserController {


    private final LongVideoUserService longVideoUserService;



    public UserController(
            LongVideoUserService longVideoUserService

    ) {
        this.longVideoUserService = longVideoUserService;
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public JsonResult login(@RequestBody UserLoginVo userLoginVo) {
        String token = longVideoUserService.login(userLoginVo.getMachineCode(), userLoginVo.getAppId());
        return JsonResult.successMap("token", token);
    }

    @ApiOperation("注册")
    @PostMapping("/join")
    public JsonResult join(@RequestBody UserJoinVo userJoinVo) {
        String token = longVideoUserService.join(userJoinVo);
        if (StrUtil.isBlank(token)) {
            return JsonResult.failed("用户注册失败");
        }
        return JsonResult.successMap("token", token);
    }

    /**
     * 获取用户信息
     *
     * @return 自定义结果集
     */
    @ApiOperation("用户信息")
    @PostMapping("/info")
    public JsonResult info() {
        UserInfoDto userInfoDto = longVideoUserService.info();
        if (userInfoDto == null) {
            return JsonResult.success(new JSONObject().set("isSuccess", false));
        }
        return JsonResult.success(userInfoDto);
    }


}
