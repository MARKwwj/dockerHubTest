package com.magic.longvideo.service.user.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.magic.framework.utils.MagicUtil;
import com.magic.framework.utils.RabbitmqUtil;
import com.magic.longvideo.mapper.LongVideoUserMapper;
import com.magic.longvideo.pojo.entity.LongVideoUserEntity;
import com.magic.longvideo.service.user.UserBindService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserBindServiceImpl implements UserBindService {

    private final LongVideoUserMapper mapper;

    public UserBindServiceImpl(LongVideoUserMapper mapper) {
        this.mapper = mapper;
    }

    public static void main(String[] args) {

    }

    private static final int MIN_USER_ID = 100000;

    @Override
    public boolean bindCode(Long userId, String code) {
        int agentId = MagicUtil.hexToInt(code);
        if (agentId < MIN_USER_ID) {
            return false;
        }
        LongVideoUserEntity agent = mapper.selectById(agentId);
        if (agent == null) {
            return false;
        }
        Long agentParentId = agent.getParentId();
        Integer agentAgentId = agent.getAgentId();
        Long agentUserId = agent.getUserId();
        if (agentUserId.equals(userId) || agentAgentId.equals(userId) || agentParentId.equals(userId)) {
            return false;
        }
        UpdateWrapper<LongVideoUserEntity> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", userId);
        updateWrapper.isNull("parent_id");
        Date bindTime = new Date();
        LongVideoUserEntity entity = new LongVideoUserEntity()
                .setAgentId(agentAgentId)
                .setParentId(agentUserId)
                .setBindTime(bindTime);
        int row = mapper.update(entity, updateWrapper);
        if (row == 1) {
            //异步处理代理绑定用户后的相关逻辑
            if (agentUserId != null && agentUserId != 0) {
                Map<String, Object> userBindParam = MapUtil.createMap(HashMap.class);
                userBindParam.put("bindUserId", entity.getUserId());
                userBindParam.put("appId", 1);
                userBindParam.put("agentUserId", agentUserId);
                userBindParam.put("userType", entity.getUserType());
                userBindParam.put("userLevel", entity.getUserLevel() + 1);
                userBindParam.put("equipmentType", entity.getEquipmentType());
                userBindParam.put("createTime", bindTime);
                userBindParam.put("parentUserId", entity.getParentId());
                RabbitmqUtil.sendObject("bindUser", userBindParam);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean bindPhone(Long userId, String phoneNumber, String verificationCode) {
        if (MagicUtil.checkVerificationCode(phoneNumber, verificationCode)) {
            UpdateWrapper<LongVideoUserEntity> updateWrapper = new UpdateWrapper();
            updateWrapper.eq("user_id", userId);
            updateWrapper.isNull("phone_number");
            int row = mapper.update(new LongVideoUserEntity().setPhoneNumber(phoneNumber), updateWrapper);
            return row == 1;
        }
        return false;
    }


}
