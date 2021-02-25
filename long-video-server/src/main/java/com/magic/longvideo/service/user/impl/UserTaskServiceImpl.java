package com.magic.longvideo.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.manager.UserManager;
import com.magic.longvideo.mapper.LongVideoTaskInfoMapper;
import com.magic.longvideo.mapper.LongVideoUserTaskMapper;
import com.magic.longvideo.pojo.dto.LongVideoTaskInfoDto;
import com.magic.longvideo.pojo.dto.UserTaskListDto;
import com.magic.longvideo.pojo.entity.LongVideoTaskInfoEntity;
import com.magic.longvideo.service.user.UserTaskService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class UserTaskServiceImpl implements UserTaskService {

    /**
     * 存储在Redis中的Key前缀: USER_TASK_MAP_PREFIX + 用户ID
     * 用来维护用户任务相关的键值对，如：
     * 1.USER_TASK_MAP_MODE_PREFIX
     * 2.USER_TASK_MAP_STATUS_PREFIX
     */
    private static final String USER_TASK_MAP_PREFIX = "USER_TASK_MAP_";

    /**
     * 存储在Redis USER_TASK_MAP_PREFIX + 用户ID 这个Map中的键值对之一: USER_TASK_MAP_PREFIX + 任务模式
     * 用来维护指定用户完成某一类任务的进度
     * 任务模式：
     * 1：邀请任务模式 - 递增数字
     * 2：保存二维码模式 - 0 或 1
     * 3：绑定手机号模式 - 0 或 1
     * 4：绑定邀请码模式 - 0 或 1
     * 5：观看视频累计时长模式 - 递增数字
     * 6：观看直播累计时长模式 - 递增数字
     * 7：点击广告模式 - 递增数字
     */
    private static final String USER_TASK_MAP_MODE_PREFIX = "USER_TASK_MAP_MODE_";

    /**
     * 存储在Redis USER_TASK_MAP_PREFIX + 用户ID 这个Map中的键值对之一: USER_TASK_MAP_STATUS_PREFIX + 任务ID
     * 用来维护指定用户某一个任务的状态
     * 任务状态：
     * 1：未完成-去做任务
     * 2：完成-待领取
     * 3：已领取
     */
    private static final String USER_TASK_MAP_STATUS_PREFIX = "USER_TASK_MAP_STATUS_";

    /**
     * 用于区分每日任务，参数及作用同上
     */
    private static final String USER_DAILY_TASK_MAP_PREFIX = "USER_DAILY_TASK_MAP_";

    private static final String USER_DAILY_TASK_MAP_MODE_PREFIX = "USER_DAILY_TASK_MAP_MODE_";

    private static final String USER_DAILY_TASK_MAP_STATUS_PREFIX = "USER_DAILY_TASK_MAP_STATUS_";


    /**
     * 任务类型参数
     */
    private static final int USER_DAILY_TASK = 3;


    /**
     * 任务状态
     */
    private static final int USER_TASK_STATUS_UNFINISHED = 1;
    private static final int USER_TASK_STATUS_COMPLETE = 2;
    private static final int USER_TASK_STATUS_RECEIVED = 3;


    /**
     * 奖励类型
     */

    private static final int USER_TASK_AWARD_TYPE_COINS = 1;
    private static final int USER_TASK_AWARD_TYPE_VIP = 2;

    private final LongVideoTaskInfoMapper longVideoTaskInfoMapper;

    private final LongVideoUserTaskMapper longVideoUserTaskMapper;

    private final UserManager userManager;

    public UserTaskServiceImpl(
            LongVideoTaskInfoMapper longVideoTaskInfoMapper,
            LongVideoUserTaskMapper longVideoUserTaskMapper,
            UserManager userManager
    ) {
        this.userManager = userManager;
        this.longVideoUserTaskMapper = longVideoUserTaskMapper;
        this.longVideoTaskInfoMapper = longVideoTaskInfoMapper;
    }

    @Override
    public List<UserTaskListDto> info() {
        QueryWrapper<LongVideoTaskInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("task_type", "sort");
        List<LongVideoTaskInfoEntity> longVideoTaskInfoList = longVideoTaskInfoMapper.selectList(queryWrapper);
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        Map<String, String> userTaskMap = JedisUtil.hGet(USER_TASK_MAP_PREFIX + userId);
        Map<String, String> userDailyTaskMap = JedisUtil.hGet(USER_DAILY_TASK_MAP_PREFIX + userId);
        if (userTaskMap == null || userDailyTaskMap == null) {
            return null;
        }
        List<UserTaskListDto> userTaskListDtoList = new ArrayList<>();
        UserTaskListDto userTaskListDto = null;
        int curType = 0;
        for (int index = 0; index < longVideoTaskInfoList.size(); index++) {
            LongVideoTaskInfoEntity entity = longVideoTaskInfoList.get(index);
            if (curType != entity.getTaskType()) {
                if (userTaskListDto != null) {
                    userTaskListDtoList.add(userTaskListDto);
                }
                userTaskListDto = new UserTaskListDto();
                userTaskListDto.setTaskList(new ArrayList<>());
            }
            //区分每日任务
            String modePrefixKey;
            String statusPrefixKey;
            String completeProgress;
            String taskStatus;
            if (entity.getTaskType() == USER_DAILY_TASK) {
                modePrefixKey = USER_DAILY_TASK_MAP_MODE_PREFIX + entity.getTaskMode();
                statusPrefixKey = USER_DAILY_TASK_MAP_STATUS_PREFIX + entity.getTaskId();
                //获取当前每日任务完成进度
                completeProgress = userDailyTaskMap.get(modePrefixKey);
                //获取当前用户对应每日任务的状态
                taskStatus = userDailyTaskMap.get(statusPrefixKey);
            } else {
                modePrefixKey = USER_TASK_MAP_MODE_PREFIX + entity.getTaskMode();
                statusPrefixKey = USER_TASK_MAP_STATUS_PREFIX + entity.getTaskId();
                //获取当前任务完成进度
                completeProgress = userTaskMap.get(modePrefixKey);
                //获取当前用户对应任务的状态
                taskStatus = userTaskMap.get(statusPrefixKey);
            }

            LongVideoTaskInfoDto longVideoTaskInfoDto = new LongVideoTaskInfoDto();
            BeanUtil.copyProperties(entity, longVideoTaskInfoDto);
            longVideoTaskInfoDto.setTaskState(StrUtil.isBlank(taskStatus) ? USER_TASK_STATUS_UNFINISHED : Integer.parseInt(taskStatus));
            //构建用于任务进度信息
            StringBuilder taskProgressBuilder = new StringBuilder();
            taskProgressBuilder.append(entity.getTaskContent());
            taskProgressBuilder.append(StrUtil.isBlank(completeProgress) ? 0 : Integer.parseInt(completeProgress) >= entity.getTarget() ? entity.getTarget() : completeProgress);
            taskProgressBuilder.append("/");
            taskProgressBuilder.append(entity.getTarget());
            longVideoTaskInfoDto.setTaskProgress(taskProgressBuilder.toString());
            longVideoTaskInfoDto.setTaskType(entity.getTaskType());
            longVideoTaskInfoDto.setTaskLogo(entity.getTaskLogo());
            userTaskListDto.getTaskList().add(longVideoTaskInfoDto);
            userTaskListDto.setTaskName("任务" + entity.getTaskType());
            userTaskListDto.setIndex(index);
            curType = entity.getTaskType();
        }
        userTaskListDtoList.add(userTaskListDto);
        return userTaskListDtoList;
    }

    @Override
    public boolean increaseTaskProgressById(Integer taskMode, Integer taskType, int increaseNum) {
        String mapPrefixKey;
        String modePrefixKey;
        String statusPrefixKey;
        String longVideoUserTaskTableFieldName;
        int second = 0;
        if (taskType == USER_DAILY_TASK) {
            longVideoUserTaskTableFieldName = "user_daily_task_map";
            mapPrefixKey = USER_DAILY_TASK_MAP_PREFIX;
            modePrefixKey = USER_DAILY_TASK_MAP_MODE_PREFIX;
            statusPrefixKey = USER_DAILY_TASK_MAP_STATUS_PREFIX;
            second = getNowToTomorrowSecond();
        } else {
            longVideoUserTaskTableFieldName = "user_task_map";
            mapPrefixKey = USER_TASK_MAP_PREFIX;
            modePrefixKey = USER_TASK_MAP_MODE_PREFIX;
            statusPrefixKey = USER_TASK_MAP_STATUS_PREFIX;
        }
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        Map<String, String> userTaskMap = JedisUtil.hGet(mapPrefixKey + userId);
        if (userTaskMap == null) {
            return false;
        }
        String taskModeTemp = userTaskMap.get(modePrefixKey + taskMode);
        int taskModeCounts = StrUtil.isBlank(taskModeTemp) ? 0 : Integer.parseInt(taskModeTemp);
        QueryWrapper<LongVideoTaskInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("target", taskModeCounts + increaseNum);
        queryWrapper.eq("task_mode", taskMode);
        List<LongVideoTaskInfoEntity> longVideoTaskInfoEntityList = longVideoTaskInfoMapper.selectList(queryWrapper);
        if (longVideoTaskInfoEntityList != null) {
            int incrTaskModeCounts = taskModeCounts + increaseNum;
            userTaskMap.put(modePrefixKey + taskMode, String.valueOf(incrTaskModeCounts));
            for (LongVideoTaskInfoEntity longVideoTaskInfoEntity : longVideoTaskInfoEntityList) {
                if (incrTaskModeCounts == longVideoTaskInfoEntity.getTarget()) {
                    userTaskMap.put(statusPrefixKey + longVideoTaskInfoEntity.getTaskId(), String.valueOf(USER_TASK_STATUS_COMPLETE));
                }
            }
            JedisUtil.hSetExpire(mapPrefixKey + userId, userTaskMap, second);
            String userTaskMapJson = JSONUtil.toJsonStr(userTaskMap);
            //TODO 异步写库?
            asyn(userId, userTaskMapJson, longVideoUserTaskTableFieldName);
        }
        return true;
    }

    @Override
    public String receiveAward(Integer taskId, Integer taskType) {
        String mapPrefixKey;
        String statusPrefixKey;
        if (taskType == USER_DAILY_TASK) {
            mapPrefixKey = USER_DAILY_TASK_MAP_PREFIX;
            statusPrefixKey = USER_DAILY_TASK_MAP_STATUS_PREFIX;
        } else {
            mapPrefixKey = USER_TASK_MAP_PREFIX;
            statusPrefixKey = USER_TASK_MAP_STATUS_PREFIX;
        }
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        Map<String, String> userTaskMap = JedisUtil.hGet(mapPrefixKey + userId);
        if (userTaskMap != null || userTaskMap.size() != 0) {
            String taskStatus = userTaskMap.get(statusPrefixKey + taskId);
            if (USER_TASK_STATUS_COMPLETE == Integer.parseInt(taskStatus)) {
                userTaskMap.put(statusPrefixKey + taskId, String.valueOf(USER_TASK_STATUS_RECEIVED));
                JedisUtil.hSet(mapPrefixKey + userId, userTaskMap);
                //TODO 异步写库？
                QueryWrapper<LongVideoTaskInfoEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("task_id", taskId);
                LongVideoTaskInfoEntity longVideoTaskInfoEntity = longVideoTaskInfoMapper.selectOne(queryWrapper);
                int award = longVideoTaskInfoEntity.getAward();
                int awardType = longVideoTaskInfoEntity.getAwardType();
                switch (awardType) {
                    case USER_TASK_AWARD_TYPE_COINS:
                        userManager.addCoins(award);
                        break;
                    case USER_TASK_AWARD_TYPE_VIP:
                        userManager.addVipTime(award);
                        break;
                    default:
                        break;
                }
                return longVideoTaskInfoEntity.getTaskReceiveNotice();
            }
        }
        return "领取奖励失败";
    }

    private int getNowToTomorrowSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return (int) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
    }

    private void asyn(Long userId, String userTaskMapJson, String longVideoUserTaskTableFieldName) {
        longVideoUserTaskMapper.insertOnDuplicateKeyUpdate(userId, userTaskMapJson, longVideoUserTaskTableFieldName);
    }


}

