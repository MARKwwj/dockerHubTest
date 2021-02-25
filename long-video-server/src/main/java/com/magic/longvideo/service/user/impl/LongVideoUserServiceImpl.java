package com.magic.longvideo.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.utils.MagicDateUtil;
import com.magic.framework.utils.MagicUtil;
import com.magic.framework.utils.RabbitmqUtil;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.mapper.LongVideoUserMapper;
import com.magic.longvideo.pojo.dto.UserInfoDto;
import com.magic.longvideo.pojo.entity.LongVideoUserEntity;
import com.magic.longvideo.pojo.vo.UserJoinVo;
import com.magic.longvideo.service.user.LongVideoUserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LongVideoUserServiceImpl implements LongVideoUserService {

    private final LongVideoUserMapper longVideoUserMapper;

    public LongVideoUserServiceImpl(LongVideoUserMapper longVideoUserMapper) {
        this.longVideoUserMapper = longVideoUserMapper;
    }

    @Override
    public String join(UserJoinVo userJoinVo) {
        Long avatarListLen = JedisUtil.lLen("OPERATE_1");
        Long nickNameListLen = JedisUtil.lLen("OPERATE_2");
        int avatarIndex = RandomUtil.randomInt(avatarListLen.intValue());
        int nickNameIndex = RandomUtil.randomInt(nickNameListLen.intValue());
        String avatar = JedisUtil.lIndex("OPERATE_1", avatarIndex);
        String nickName = JedisUtil.lIndex("OPERATE_2", nickNameIndex);
        LongVideoUserEntity entity = new LongVideoUserEntity();
        BeanUtil.copyProperties(userJoinVo, entity);
        Date createTime = new Date();
        entity
                .setAvatar(avatar)
                .setNickName(nickName)
                .setUserLevel(userJoinVo.getUserLevel())
                .setParentId(userJoinVo.getParentId())
                .setAgentId(userJoinVo.getAgentId())
                .setMachineCode(userJoinVo.getMachineCode())
                .setUserOrigin(userJoinVo.getUserOrigin())
                .setMobileType(userJoinVo.getMobileType())
                .setEquipmentType(userJoinVo.getEquipmentType())
                .setCreateTime(createTime)
                .setLastLoginTime(new Date());
        if (longVideoUserMapper.insert(entity) == 1) {
            //TODO 异步处理代理相关逻辑
            if (userJoinVo.getAgentId() != null && userJoinVo.getAgentId() != 0) {
                Map<String, Object> userBindParam = MapUtil.createMap(HashMap.class);
                userBindParam.put("bindUserId", entity.getUserId());
                userBindParam.put("appId", userJoinVo.getAppId());
                userBindParam.put("agentUserId", userJoinVo.getAgentId());
                userBindParam.put("userType", userJoinVo.getUserType());
                userBindParam.put("userLevel", userJoinVo.getUserLevel());
                userBindParam.put("equipmentType", userJoinVo.getEquipmentType());
                userBindParam.put("createTime", createTime);
                userBindParam.put("parentUserId", userJoinVo.getParentId());
                RabbitmqUtil.sendObject("bindUser", userBindParam);
            }
            return TokenUtil.generateTokenAndSaveRedis(entity.getUserId(), userJoinVo.getAppId());
        }
        return null;
    }

    @Override
    public String login(String machineCode, Integer appId) {
        QueryWrapper<LongVideoUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id");
        queryWrapper.eq("machine_code", machineCode);
        queryWrapper.eq("status", 0);
        LongVideoUserEntity entity = longVideoUserMapper.selectOne(queryWrapper);
        if (entity == null) {
            return null;
        }
        String token = TokenUtil.generateTokenAndSaveRedis(entity.getUserId(), appId);
        return token;
    }

    @Override
    public UserInfoDto info() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        QueryWrapper<LongVideoUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        LongVideoUserEntity entity = longVideoUserMapper.selectOne(queryWrapper);
        if (entity == null) {
            return null;
        }
        UserInfoDto UserInfoDto = new UserInfoDto();
        BeanUtil.copyProperties(entity, UserInfoDto);
        if (UserInfoDto.getAgentId() != null) {
            UserInfoDto.setBindCode(MagicUtil.intToHex(UserInfoDto.getAgentId()));
        }
        Date vipEndTime = entity.getVipEndTime();
        if (vipEndTime != null) {
            int now = DateUtil.toIntSecond(new Date());
            long vipEndTimeSecond = MagicDateUtil.toLongSecond(vipEndTime);
            String vipEndDate = DateUtil.format(vipEndTime, "yyyy-MM-dd HH:mm:ss");
            UserInfoDto.setVipEndTime((vipEndTimeSecond - now) < 0 ? "已过期" : vipEndDate);
            if (StrUtil.isBlank(entity.getPhoneNumber())) {
                UserInfoDto.setRechargeStatus(1);
            }
        } else if (entity.getGoldCoins() > 0 && StrUtil.isBlank(entity.getPhoneNumber())) {
            UserInfoDto.setRechargeStatus(1);
        }
        return UserInfoDto;
    }


}
