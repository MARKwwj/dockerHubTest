package com.magic.longvideo.controller.user;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.MagicUtil;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.manager.UserManager;
import com.magic.longvideo.mapper.LongVideoAppRealmMapper;
import com.magic.longvideo.pojo.dto.LongVideoTaskInfoDto;
import com.magic.longvideo.pojo.dto.LongVideoUserDto;
import com.magic.longvideo.pojo.dto.UserTaskListDto;
import com.magic.longvideo.pojo.entity.LongVideoAppRealmEntity;
import com.magic.longvideo.pojo.vo.ReceiveAwardVo;
import com.magic.longvideo.pojo.vo.UserTaskIncreaseProgressVo;
import com.magic.longvideo.service.user.UserTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/long_video/user/task")
@Api(tags = "[任务列表，任务进度增长(用于观看时长任务)，获取奖励]")
public class UserTaskController {


    private final UserTaskService userTaskService;

    private final UserManager userManager;

    private final LongVideoAppRealmMapper longVideoAppRealmMapper;

    public UserTaskController(
            LongVideoAppRealmMapper longVideoAppRealmMapper,
            UserTaskService userTaskService,
            UserManager userManager
    ) {
        this.longVideoAppRealmMapper = longVideoAppRealmMapper;
        this.userManager = userManager;
        this.userTaskService = userTaskService;
    }

    @PostMapping
    @ApiOperation("获取任务列表内容")
    public JsonResult info() {
        List<UserTaskListDto> info = userTaskService.info();
        Map<String, Object> result = new HashMap<>(4);
        result.put("tag", info);
        //TODO 用户推广信息
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        LongVideoUserDto longVIdeoUserDto = userManager.queryUserInfoById(userId);
        StringBuilder urlParamsBuilder = new StringBuilder();
        QueryWrapper<LongVideoAppRealmEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("del_flag", 2);
        List<LongVideoAppRealmEntity> longVideoAppRealmEntityList = longVideoAppRealmMapper.selectList(queryWrapper);
        int index = RandomUtil.randomInt(longVideoAppRealmEntityList.size());
        String link = longVideoAppRealmEntityList.get(index).getRealmUrl();
        urlParamsBuilder.append(link);
        urlParamsBuilder.append("?userOrigin=0");
        urlParamsBuilder.append("&agentId=");
        urlParamsBuilder.append(longVIdeoUserDto.getAgentId());
        urlParamsBuilder.append("&parentId=");
        urlParamsBuilder.append(longVIdeoUserDto.getUserId());
        urlParamsBuilder.append("&userLevel=");
        urlParamsBuilder.append(longVIdeoUserDto.getUserLevel() + 1);
        urlParamsBuilder.append("&appId=1");
        result.put("link", urlParamsBuilder.toString());
        result.put("code", MagicUtil.intToHex(userId.intValue()));
        return JsonResult.success(result);
    }

    @ApiOperation("任务进度增长")
    @PostMapping("/increase/progress")
    public JsonResult<Boolean> increaseTaskProgressById(@RequestBody UserTaskIncreaseProgressVo vo) {
        boolean flag = userTaskService.increaseTaskProgressById(vo.getTaskMode(), vo.getTaskType(), vo.getIncreaseNum());
        return JsonResult.success(flag);
    }

    @ApiOperation("领取奖励")
    @PostMapping("/receive/award")
    public JsonResult<String> receiveAward(@RequestBody ReceiveAwardVo vo) {
        return JsonResult.success(userTaskService.receiveAward(vo.getTaskId(), vo.getTaskType()));
    }

    @PostMapping("/delete/cache")
    public JsonResult<Map<Integer, List<LongVideoTaskInfoDto>>> deleteCache(@RequestBody(required = false) Long userId) {
        if (userId == null) {
            userId = TokenUtil.getUserIdByTokenFromHeader();
        }
        JedisUtil.del("USER_TASK_MAP_" + userId);
        JedisUtil.del("USER_DAILY_TASK_MAP_" + userId);
        return JsonResult.success();
    }

}
